package ru.smarthome.server.http.handlers;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import ru.smarthome.arduinodevices.ArduinoControllerResponse;
import ru.smarthome.arduinodevices.controllers.ArduinoReadable;
import ru.smarthome.library.BaseController;

public class ControllerGet extends BaseRequestHandler{

    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        Map<String, String> params = session.getParms();
        BaseController controller = getController(params);
        if (controller instanceof ArduinoReadable) {
            try {
                ArduinoControllerResponse response = ((ArduinoReadable) controller).read();
                return new NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, "text/json",
                        new Gson().toJson(response));
            } catch (IOException e) {
                return getArduinoHttpError();
            }
        } else {
            throw new IllegalStateException("get request to non readable service");
        }
    }
}
