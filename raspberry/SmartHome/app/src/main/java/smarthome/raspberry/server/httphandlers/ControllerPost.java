package smarthome.raspberry.server.httphandlers;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import smarthome.library.common.BaseController;
import smarthome.raspberry.arduinodevices.ArduinoControllerResponse;
import smarthome.raspberry.arduinodevices.controllers.ArduinoWritable;

public class ControllerPost extends BaseRequestHandler {

    public static final String TAG = ControllerPost.class.getSimpleName();

    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        Map<String, String> params = session.getParms();
        try {
            BaseController controller = getController(params);

            if (controller instanceof ArduinoWritable) {
                String value = params.get("value");
                ArduinoControllerResponse response = ((ArduinoWritable) controller).write(value);
                return new NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, "text/json", new Gson().toJson(response));
            }

            return getInvalidRequestResponse("post request to non writable controller " + controller);
        } catch (IllegalArgumentException e) {
            return getInvalidRequestResponse(e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "request to arduino web server failed: " + e);
            return getArduinoHttpError();
        }
    }
}
