package smarthome.raspberry.server;

import android.net.Uri;
import android.util.Log;

import fi.iki.elonen.NanoHTTPD;
import smarthome.raspberry.server.httphandlers.AlertPost;
import smarthome.raspberry.server.httphandlers.ControllerGet;
import smarthome.raspberry.server.httphandlers.ControllerPost;
import smarthome.raspberry.server.httphandlers.ErrorHandler;
import smarthome.raspberry.server.httphandlers.InfoGet;
import smarthome.raspberry.server.httphandlers.InitPost;
import smarthome.raspberry.server.httphandlers.RequestHandler;
import smarthome.raspberry.server.httphandlers.ResetPost;

import static fi.iki.elonen.NanoHTTPD.Method.GET;
import static fi.iki.elonen.NanoHTTPD.Method.POST;

public enum HandlerType {
    READ_CONTROLLER(GET, "/controller", new ControllerGet()),
    CHANGE_CONTROLLER(POST, "/controller", new ControllerPost()),
    INFO(GET, "/info", new InfoGet()),
    INIT(POST, "/init", new InitPost()),
    RESET(POST, "/reset", new ResetPost()),
    ALERT(POST, "/alert", new AlertPost());


    public static final String TAG = HandlerType.class.getSimpleName();
    private static final ErrorHandler errorHandler = new ErrorHandler();
    private RequestHandler handler;
    private NanoHTTPD.Method method;
    private String requestPath;

    HandlerType(NanoHTTPD.Method method, String requestPath, RequestHandler handler) {
        this.method = method;
        this.requestPath = requestPath;
        this.handler = handler;
    }

    public static NanoHTTPD.Response handle(NanoHTTPD.IHTTPSession session) {
        NanoHTTPD.Method method = session.getMethod();
        String uri = session.getUri();
        Log.d(TAG, "handle: " + method + ":" + uri + " " + session.getQueryParameterString());

        HandlerType handlerType = findSuitableHandler(method, uri);
        return handlerType.handler.serve(session);
    }

    public static NanoHTTPD.Response errorHandle(String message) {
        return errorHandler.getError(message);
    }

    private static HandlerType findSuitableHandler(NanoHTTPD.Method method, String uri) {
        for (HandlerType handlerType : values()) {
            if (handlerType.method == method && uri.startsWith(handlerType.requestPath)) {
                return handlerType;
            }
        }

        throw new IllegalArgumentException("No handler found for " + method + ", " + uri);
    }
}
