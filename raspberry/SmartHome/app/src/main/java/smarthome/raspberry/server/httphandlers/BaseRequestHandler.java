package smarthome.raspberry.server.httphandlers;

import android.support.annotation.NonNull;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import smarthome.library.common.BaseController;
import smarthome.raspberry.model.SmartHomeRepository;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;

public abstract class BaseRequestHandler implements RequestHandler{

    @NonNull
    protected NanoHTTPD.Response getInvalidRequestResponse(String message) {
        return new NanoHTTPD.Response(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Invalid request: " + message);
    }

    protected BaseController getController(Map<String, String> params) {
        // todo add checks so it won't crash
        long controllerGuid = Long.parseLong(params.get("controller_guid"));
        return SmartHomeRepository.getInstance().getController(controllerGuid);
    }

    @NonNull
    protected NanoHTTPD.Response getArduinoHttpError() {
        return new NanoHTTPD.Response(NanoHTTPD.Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "arduino web server not available");
    }

}
