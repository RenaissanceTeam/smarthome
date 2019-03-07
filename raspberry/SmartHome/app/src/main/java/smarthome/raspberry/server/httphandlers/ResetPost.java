package smarthome.raspberry.server.httphandlers;

import fi.iki.elonen.NanoHTTPD;
import smarthome.raspberry.model.SmartHomeRepository;

public class ResetPost extends BaseRequestHandler {
    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        SmartHomeRepository.getInstance().removeAll();
        return new NanoHTTPD.Response("Everything is deleted");
    }
}
