package ru.smarthome.server.http.handlers;

import android.support.annotation.NonNull;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import ru.smarthome.library.BaseController;
import ru.smarthome.model.RaspberrySmartHome;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;

public abstract class BaseRequestHandler implements RequestHandler{

    protected BaseController getController(Map<String, String> params) {
        // todo add checks so it won't crash
        long controllerGuid = Long.parseLong(params.get("controller_guid"));
        return RaspberrySmartHome.getInstance().getController(controllerGuid);
    }

    @NonNull
    protected NanoHTTPD.Response getArduinoHttpError() {
        return new NanoHTTPD.Response(NanoHTTPD.Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "arduino web server not available");
    }

}
