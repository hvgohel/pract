(function(){
    NamespaceManager.register('com.dw.servernotificationlistener');
    var o = com.dw.servernotificationlistener;
    
    o.Cometd = o.Base.extend({
        _bConnected : false,        // current connected status
        
        // PRIVATE FUNCTIONS
        initialize : function()
        {
            var self = this;
            o.Base.prototype.initialize.apply(self, arguments);
            
            self._metaConnect = _.bind(self._metaConnect, self);
            self._metaHandshake = _.bind(self._metaHandshake, self);
            
            $.cometd.unregisterTransport('websocket');
            
            // cometd listerner exception handler, it throws whatever error that was received.
            $.cometd.onListenerException = function(x, handle, listener, message){
                window.setTimeout(function(){
                    throw x;
                }, 0);
            };
        
            $.cometd.configure({
                url: self._sFullServerUrl,
                autoBatch : true,
                maxNetworkDelay : 800000
            });
        
            $.cometd.addListener('/meta/handshake', self._metaHandshake);
            $.cometd.addListener('/meta/connect', self._metaConnect);
            $.cometd.addListener('/meta/subscribe', function(message){
                message = message || {};
                
                if(message.successful == true)
                    self._onSubscriptionSuccess(message.subscription);
                else if(message.successful == false)
                    self._onSubscriptionError(message.subscription, message.error);
            });
            
        },
        
        // Function that manages the connection status with the Bayeux server
        _metaConnect : function(message)
        {
            var self = this;
            
            var oStatus = self._oStatus;
            if ($.cometd.isDisconnected())
            {
                self._bConnected = false;
                self._setStatus(oStatus.CLOSED);
                return;
            }

            self._wasConnected = self._bConnected;
            self._bConnected = message.successful === true;
            if (!self._wasConnected && self._bConnected)
            {
                self._setStatus(oStatus.ESTABLISHED);
            }
            else if (self._wasConnected && !self._bConnected)
            {
                self._setStatus(oStatus.BROKEN);
                // cometd automatically retries connection.
                self._setBrokenStatus(self._oBrokenStatus.AUTOCONNECT_INPROGRESS);
            }
        },
        
        // Function invoked when first contacting the server and
        // when the server has lost the state of this client
        _metaHandshake : function(handshake)
        {
            var self = this;
            if (handshake.successful === true)
            {
                self._onHandshakeSuccess(handshake);
                return;
            }
            else 
            {
                self._onHandshakeFail(handshake);
            }
        },
        
        // called when any response is received from channel
        // should return parse message.data
        _parseMessage : function(message)
        {
            if(!message)
                return {};
            
            var oData;
            
            try{
                oData = $.parseJSON(message.data);
            }catch (e){
                oData = message.data;
            }
            
            if(!oData)
                oData = message.data;
            
            return {
                channel : message.channel,
                data :  oData
            };
        },
        
        _subscribeChannel : function(channel)
        {
            var self = this;
            var handle = $.cometd.subscribe(channel, function(message){
                self._onPushMessage(channel, message);
            });
            
            self._addSubscriptionHandle(channel, handle);
        },
        
        _unsubscribeChannel : function(channel)
        {
            var self = this;
            // TODO: Chetan: Is this proper way to unsubscribe? I assume this is wrong as its passing array.
            $.cometd.unsubscribe(self._oSubscriptionHandles[channel]);
            self._removeSubscriptionHandle(self._oSubscriptionHandles[channel]);
        },
        
        // PUBLIC FUNCTIONS
        connect : function()
        {
            var self = this;
            
            if(!self._canConnect())
                throw "instance status does not allow to connect, status is : " + self.getStatus();
            
            self._setStatus(self._oStatus.INPROGRESS);
            $.cometd.handshake({
                ext : {
                    accessToken : self.options.accessToken,
                    data : self.options.data
                },
            });
        },

        disconnect : function()
        {
            $.cometd.disconnect(true);
            this._removeAllEvents();
        }
    });
})();
