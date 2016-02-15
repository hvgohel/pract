(function()
{
    NamespaceManager.register('com.dw.servernotificationlistener');
    var o = com.dw.servernotificationlistener;

    o.Base = Backbone.Model.extend({
        // PRIVATE VARIABLES
        _oSubscriptionHandles : undefined, // handles for subscribed channel (channel : handle), its cometd or any push server handles, not backbone events
        options : undefined,
        _oEvents : undefined, // eventName : [handler, handler]
        _oStatus : undefined,
        _oBrokenStatus : undefined,

        // Current status of instance
        _sFullServerUrl : undefined, // http://localhost:8080/cometd

        _bConnectedOnce : false, // was instance ever connected to server? used to determine when to call options.onConnect callback
        _bAccessTokenRetrived : false,

        // PRIVATE METHODS
        initialize : function(options)
        {
            var self = this;
            self.options = $.extend(true, {}, self.constructor.defOptions, options);
            self._oEvents = {};
            self._oSubscriptionHandles = {};
            self._oStatus = self.constructor.status;
            self._oErrorStatus = self.constructor.errorStatus;
            self._oBrokenStatus = self.constructor.brokenStatus;

            self._bAccessTokenRetrived = false;
            self._resetAccessTokenRetryNumber();

            var sPushServerUrl = self.options.pushServerUrl;
            if (sPushServerUrl.charAt(0) == '/')
                sPushServerUrl = sPushServerUrl.slice(1);

            self._sFullServerUrl = self.options.baseUrl + (self.options.baseUrl.charAt(self.options.baseUrl.length - 1) == '/' ? '' : '/') + sPushServerUrl;
            self._setStatus(self._oStatus.PENDING);
            self._bindEvents();
            self._bindModelEvents();
        },

        // add event listeners on this backbone model (not to server)
        _addEventListener : function(channel, handler)
        {
            var self = this;
            if (!self._oEvents[channel])
                self._oEvents[channel] = [];

            self._oEvents[channel].push(handler);
        },

        // remove event listeners on this backbone model (not to server)
        _removeEventListener : function(channel, handler)
        {
            var self = this;
            if (!self._oEvents[channel])
                return;

            if (!handler)
            {
                delete self._oEvents[channel];
                return;
            }

            // WARN: intentionally modifiy self._oEvents[channel] array reference (discarding old reference)
            self._oEvents[channel] = _.without(self._oEvents[channel], handler);
            if (!self._oEvents[channel].length)
                delete self._oEvents[channel];
        },

        _removeAllEvents : function()
        {
            this._oEvents = {};
        },

        // Disconnect when the page unloads
        _bindEvents : function()
        {
            var self = this;
            $(window).unload(function(e)
            {
                self.disconnect();
            });
        },

        _bindModelEvents : function()
        {
            var self = this;
            self.on('change:status', self._onStatusChange, self);
        },

        _onStatusChange : function(model)
        {
            var self = this;
            var status = self.getStatus();
            switch (status)
            {
            case self._oStatus.ESTABLISHED:
                if (!self._bConnectedOnce)
                    return;

                if (!self._wasConnected && self._bConnected)
                    return;
                
                window.console && console.log("resubscribing all channels as connection was broken");
                self._resubscribeAllChannels();
                break;
            default:
                return;
            }
        },

        _canConnect : function()
        {
            var self = this;
            var sCurrentStatus = self.getStatus();
            var oStatus = self._oStatus;
            switch (sCurrentStatus)
            {
            case oStatus.PENDING:
            case oStatus.BROKEN:
            case oStatus.CLOSED:
                return true;
                break;
            default:
                return false;
                break;
            }
        },

        // when there is an array with oEvents[channel] exists
        _isChannelSubscribed : function(channel)
        {
            var self = this;

            if (!channel)
                throw "insufficient arguments";

            return channel in self._oEvents ? true : false;
        },

        // notify all handlers bound to push manager
        _notify : function(channel, data, receivedChannel)
        {
            channel && this.trigger(channel, data, receivedChannel);
        },

        // whenever we receive message from server
        _onPushMessage : function(channel, message)
        {
            var self = this;
            var oData = self._parseMessage(message);
            self._notify(channel, oData.data, oData.channel);
        },

        _onHandshakeSuccess : function()
        {
            var self = this;
            self._setStatus(self._oStatus.ESTABLISHED);
            self._bAccessTokenRetrived = false;
            if (!self._bConnectedOnce)
            {
                self._bConnectedOnce = true;
                self.options.onConnect && self.options.onConnect();
            }
        },

        _onHandshakeFail : function(handshake)
        {
            var self = this;
            self._setStatus(self._oStatus.BROKEN);

            if (!self._bConnectedOnce)
            {
                window.console && console.log('DEAD: conncetion was never established, DEAD state');
                self._setBrokenStatus(self._oBrokenStatus.DEAD);
                return;
            }

            if (self._bConnectedOnce && !self._bAccessTokenRetrived)
            {
                window.console && console.log('Client was connected to server atleast Once, but never retrived new Access Token');
                self._setBrokenStatus(self._oBrokenStatus.RETRIVING_ACCESS_TOKEN);
                self._retriveAccessToken();
            }
            else if (!self._bAccessTokenRetrived)
            {
                window.console && console.log('Never retrived new access token from server');
                self._setBrokenStatus(self._oBrokenStatus.RETRIVING_ACCESS_TOKEN);
                self._retriveAccessToken();
            }
            else if (self._bAccessTokenRetrived)
            {
                window.console && console.log('DEAD: Access token was retrived, but cannot establish connection, DEAD state');
                self._setBrokenStatus(self._oBrokenStatus.DEAD);
            }
        },

        _retriveAccessToken : function()
        {
            var self = this;
            self._bAccessTokenRetrived = false;
            $.ajax({
                url : self.options.accessTokenUrl,
                success : function(response)
                {
                    self._onAccessTokenRetriveSuccess.apply(self, arguments);
                },
                error : function(error, status)
                {
                    self._onAccessTokenRetriveError.apply(self, arguments);
                }
            });
        },

        _onAccessTokenRetriveSuccess : function(response)
        {
            var self = this;
            self._bAccessTokenRetrived = true;
            self._resetAccessTokenRetryNumber();
            self.options.accessToken = response;
            self.connect();
        },

        _onAccessTokenRetriveError : function(error, status)
        {
            var self = this;
            // server error

            if (status == "timeout")
            {
                // just prevent it going into below condition :D
            }
            else if (error.status < 600 || error.status > 499)
            {
                self._setBrokenStatus(self._oBrokenStatus.DEAD);
                return;
            }

            window.setTimeout(function()
            {
                self._retriveAccessToken();
            }, 1000 * self._getAccessTokenRetryNumber());
        },

        _getAccessTokenRetryNumber : function()
        {
            return this._nAccessTokenRetryNumber++;
        },

        _resetAccessTokenRetryNumber : function()
        {
            this._nAccessTokenRetryNumber = 1;
        },

        // format data as needed, child should override (e.g JSON)
        _parseMessage : function(message)
        {
            return message;
        },

        _addSubscriptionHandle : function(channel, handle)
        {
            channel && handle && (this._oSubscriptionHandles[channel] = handle);
        },

        _removeSubscriptionHandle : function(channel)
        {
            delete this._oSubscriptionHandles[channel];
        },

        _subscribeChannel : function(channel)
        {
            throw "abstract";
        },

        _resubscribeAllChannels : function()
        {
            var self = this;
            var aEvents = _.keys(self._oEvents);
            _.each(aEvents, function(channel)
            {
                window.console && console.log('subscribing channel : ' + channel);
                self._removeSubscriptionHandle(channel);
                self._subscribeChannel(channel);
            });
        },

        // set instance status
        _setStatus : function(status)
        {
            if (status == undefined)
                throw "Insufficient arguments";

            this.set('status', status);
        },

        _setBrokenStatus : function(status)
        {
            if (status == undefined)
                throw "Insufficient arguments";

            this.set('brokenStatus', status);
        },

        // unsubscribe push channel
        _unsubscribeChannel : function(channel)
        {
            throw "abstract";
        },

        // PUBLIC METHODS
        addEventListener : function(channel, handler, onError)
        {
            var self = this;
            
            if(!channel || !handler)
                throw "Insufficient arguments";
            
            if (!self._isChannelSubscribed(channel))
            {
                self._addEventListener("error:" + channel, onError);
                self.on("error:" +channel, onError);
                self._subscribeChannel(channel);
            }

            self._addEventListener(channel, handler);
            self.on(channel, handler);
        },

        // channel is mandatory
        // handler is optional, if passed, only that hanlder will be unsubscribed
        // when not passed, all handler for that channel will be unsubscribed
        removeEventListener : function(channel, handler)
        {
            var self = this;
            
            if(!channel)
                throw "Insufficient arguments";
            
            self.off(channel, handler);
            self._removeEventListener(channel, handler);
            
            if(channel.match("error:"))
                return;

            // unsubscribe push channel if there are no more subscribed handlers for "channel"
            if (!self._isChannelSubscribed(channel))
                self._unsubscribeChannel(channel);
        },
        
        _onSubscriptionSuccess : function(channel)
        {
            var self = this;
            if(!channel)
                return;
            
            self.removeEventListener("error:" + channel);
        },
        
        _onSubscriptionError : function(channel, errorMessage)
        {
            var self = this;
            
            if(!channel)
                return;
            
            self.removeEventListener(channel);
            self.trigger("error:" + channel, errorMessage, channel);
            self.removeEventListener("error:" + channel);
            self._removeSubscriptionHandle(channel);
        },

        connect : function()
        {
            throw "abstract";
        },

        disconnect : function()
        {
            throw "abstract";
        },

        getStatus : function()
        {
            return this.get('status');
        },

        getBrokenStatus : function()
        {
            return this.get('brokenStatus');
        }

    }, {
        // CLASS constants
        defOptions : {
            baseUrl : window.location.protocol + "//" + window.location.host, // up to domain name
            pushServerUrl : "", // push server URL (/cometd)
            accessToken : "", // access token for security access
            data : {
            // Extra data for this session, server can use it. will be used while configuring instance (e.g comentd)
            },
            onConnect : function()
            {
                // called when instance is successfully connected with server
            }
        },

        brokenStatus : {
            AUTOCONNECT_INPROGRESS : 1, // connection is broken, trying to autoconnect
            RETRIVING_ACCESS_TOKEN : 2, // connection is broken, retriving new access token
            HANDSHAKING : 3, // connection is broken, trying handshake
            DEAD : 4, // connection is broken, DEAD End (fatal error)
            toString : function(status)
            {
                var self = this;
                switch (status)
                {
                case self.AUTOCONNECT_INPROGRESS:
                    return "autoconnect_inprogress";
                case self.RETRIVING_ACCESS_TOKEN:
                    return "retriving_access_token";
                case self.HANDSHAKING:
                    return "handshaking";
                case self.DEAD:
                    return "dead";
                default:
                    "not_found";
                }
            }
        },

        status : {
            PENDING : 1, // connection is never initialized ( never called connect
            INPROGRESS : 2, // connct in progress
            ESTABLISHED : 3, // connection is established
            BROKEN : 4, // connection is broken
            CLOSED : 5, // connection is closed ( was connected and disconnected at will (via disconnect method )
            toString : function(status)
            {
                var self = this;
                switch (status)
                {
                case self.PENDING:
                    return "pending";
                case self.INPROGRESS:
                    return "inprogress";
                case self.ESTABLISHED:
                    return "establised";
                case self.BROKEN:
                    return "broken";
                case self.CLOSED:
                    return "closed";
                default:
                    return "not_found";
                }
            }
        }
    });
})();