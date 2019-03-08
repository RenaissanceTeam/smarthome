package smarthome.raspberry.server.httphandlers;

import fi.iki.elonen.NanoHTTPD;

public class ErrorHandler extends BaseRequestHandler{

    public NanoHTTPD.Response getError(String message) {
        return getInvalidRequestResponse(message);
    }

    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        throw new IllegalArgumentException(ErrorHandler.class.getSimpleName() +
                " can't serve for valid session");
    }
}
