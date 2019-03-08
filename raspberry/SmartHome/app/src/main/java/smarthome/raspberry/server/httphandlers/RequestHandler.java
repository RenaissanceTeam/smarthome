package smarthome.raspberry.server.httphandlers;

import fi.iki.elonen.NanoHTTPD;

public interface RequestHandler {
    NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session);
}
