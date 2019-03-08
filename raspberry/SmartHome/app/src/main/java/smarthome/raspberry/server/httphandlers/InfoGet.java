package smarthome.raspberry.server.httphandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fi.iki.elonen.NanoHTTPD;
import smarthome.raspberry.model.SmartHomeRepository;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;

public class InfoGet extends BaseRequestHandler {
    private Gson gson;

    public InfoGet() {
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        String serializedHomeState = gson.toJson(SmartHomeRepository.getInstance());
        return new NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, serializedHomeState);
    }
}
