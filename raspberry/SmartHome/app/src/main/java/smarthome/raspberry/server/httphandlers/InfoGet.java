package smarthome.raspberry.server.httphandlers;

import fi.iki.elonen.NanoHTTPD;
import smarthome.raspberry.model.RaspberrySmartHome;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;

public class InfoGet extends BaseRequestHandler {
    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        return new NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, RaspberrySmartHome.getInstance().toString());
    }
}
