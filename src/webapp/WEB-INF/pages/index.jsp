<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Comet-Testing</title>
    <script type="text/javascript" src="public/js/lib/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="public/js/lib/cometd/cometd.js"></script>
    <script type="text/javascript" src="public/js/lib/cometd/jquery.cometd.js"></script>
    <script type="text/javascript" src="public/js/lib/underscore-min.js"></script>
    <script type="text/javascript" src="public/js/lib/backbone-min.js"></script>
    <script type="text/javascript" src="public/js/app/namespace-manager.js"></script>
    <script type="text/javascript" src="public/js/app/com.dw.servernotificationlistener.base.js"></script>
    <script type="text/javascript" src="public/js/app/com.dw.servernotificationlistener.cometd.js"></script>
    <script type="text/javascript" src="public/js/app/app.servernotificationlistener.js"></script>
    <script type="text/javascript" src="public/js/script.js"></script>

    <style>
        body {
            font-family: Arial, Verdana;
        }

        div {
            margin-bottom: 20px;
        }
    </style>

    <script type="text/javascript">
        window.accessToken = '<%= request.getAttribute("cometdAccessToken") %>';
    </script>

</head>
<body>
    <h2>Hello!!!</h2>

    <div>
        <button id="handshake">Handshake</button>
        <span class="handshake-result"></span>
    </div>

    <div>
        <button id ="broadcast">BroadCast</button>
    </div>
</body>
</html>
