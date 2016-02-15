$(function() {

    $.cometd.unregisterTransport('websocket');
    myServerNotificationListner = new MyServerNotificationListener({
        baseUrl : window.location.protocol + "//" + window.location.host, // up to domain name
        logLevel : 'info',
        // pushServerUrl : "pushnotification-test/cometd", // push server URL (/cometd)
        pushServerUrl : "/cometd", // push server URL (/cometd)
        accessToken : window.accessToken, // access token for security access
        data : {
            userId : 1
        // example of extra data
        // Extra data for this session, server can use it. will be used while configuring instance (e.g comentd)
        },
        onConnect : function() {
            console.log('Yay, we are connected now');
            // called when instance is successfully connected with server
        }
    });

    // to initializing connection from handshake button

    $("#handshake").click(function() {

        myServerNotificationListner.connect();

        console.log('listener added to /news channel');
        myServerNotificationListner.addEventListener('/news', function(data) {
            console.log("response from /news channel", data);
        });
    });

    myServerNotificationListner.on('change:status', function() {
        console.log('status changed : '
                + com.dw.servernotificationlistener.Base.status.toString(myServerNotificationListner.get('status')));
    });

    $('#broadcast').click(function() {
        $.ajax({
            // url : '/pushnotification-test/broadcast',
            url : '/broadcast',
            type : 'POST',
        });
    });
});
