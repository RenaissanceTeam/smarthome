package raspberry.smarthome;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import raspberry.smarthome.model.RaspberrySmartHome;
import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.controllers.ControllersFactory;
import raspberry.smarthome.model.device.controllers.Readable;
import raspberry.smarthome.model.device.controllers.Writable;
import raspberry.smarthome.model.device.requests.ControllerResponse;
import ru.smarthome.library.BaseController;
import ru.smarthome.library.ControllerType;
import ru.smarthome.library.IotDevice;

public class WebServer extends NanoHTTPD {

    public static final String TAG = WebServer.class.getSimpleName();

    public WebServer() {
        super(8080);
    }


    @Override
    public Response serve(IHTTPSession session) {
        Log.d(TAG, "serve: " + session + ", thread=" + Thread.currentThread());
        Method method = session.getMethod();
        String uri = session.getUri();

        if (method == Method.GET) {
            if (uri.startsWith("/controller")) {
                return makeReadRequestToDevice(session);
            }

            if (uri.startsWith("/info")) {
                // todo implement basic web interface with info about current controllers state ??
                return new Response(Response.Status.OK, MIME_PLAINTEXT, RaspberrySmartHome.getInstance().toString());
            }
        } else if (method == Method.POST) {
            if (uri.startsWith("/init")) {
                // todo add check if it's really arduino if other devices will be added the same way
                if (initNewArduinoDevice(session)) {
                    return new Response("Added successfully");
                }
                return new Response("Device was not added");
            }

            if (uri.startsWith("/reset")) {
                RaspberrySmartHome.getInstance().removeAll();
                return new Response("Everything is deleted");
            }

            if (uri.startsWith("/controller")) {
                return makeWriteRequestToDevice(session);
            }

            if (uri.startsWith("/alert")) {
                // todo implement post request for arduino controllers (notify android client about
                // some state change)
            }
        }

        return getInvalidRequestResponse("No suitable method found");
    }

    @NonNull
    private Response getInvalidRequestResponse(String message) {
        return new Response(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Invalid request: " + message);
    }

    @NonNull
    private Response makeReadRequestToDevice(IHTTPSession session) {
        Map<String, String> params = session.getParms();
        BaseController controller = getController(params);
        if (controller instanceof Readable) {
            try {
                ControllerResponse response = ((Readable) controller).read();
                return new Response(Response.Status.OK, "text/json", new Gson().toJson(response));
            } catch (IOException e) {
                return getArduinoHttpError();
            }
        } else {
            throw new IllegalStateException("get request to non readable service");
        }
    }


    @NonNull
    private Response makeWriteRequestToDevice(IHTTPSession session) {
        Map<String, String> params = session.getParms();
        try {
            BaseController controller = getController(params);

            if (controller instanceof Writable) {
                String value = params.get("value");
                ControllerResponse response = ((Writable) controller).write(value);
                return new Response(Response.Status.OK, "text/json", new Gson().toJson(response));
            }

            return getInvalidRequestResponse("post request to non writable controller " + controller);
        } catch (IllegalArgumentException e) {
            return getInvalidRequestResponse(e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "request to arduino web server failed: " + e);
            return getArduinoHttpError();
        }
    }

    @NonNull
    private Response getArduinoHttpError() {
        return new Response(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "arduino web server not available");
    }

    private BaseController getController(Map<String, String> params) {
        // todo add checks so it won't crash
        long deviceGuid = Long.parseLong(params.get("device_guid"));
        long controllerGuid = Long.parseLong(params.get("controller_guid"));

        IotDevice device = RaspberrySmartHome.getInstance().getByGuid(deviceGuid);
        if (device instanceof ArduinoIotDevice) {
            return ((ArduinoIotDevice) device).getControllerByGuid(controllerGuid);
        }
        throw new IllegalStateException("not arduino devices are not supported");
    }

    private boolean initNewArduinoDevice(IHTTPSession session) {
        Map<String, String> params = session.getParms();
        String name = params.get("name");
        String description = params.get("description");
        String ip = session.getHeaders().get("http-client-ip");

        ArduinoIotDevice device = new ArduinoIotDevice(name, description, ip);

        String[] rawServices = params.get("services").split(";");
        List<BaseController> controllers = new ArrayList<>();
        int index = 0;
        for (String rawService : rawServices) {
            int id = Integer.parseInt(rawService.trim());
            ControllerType type = ControllerType.getById(id);
            controllers.add(ControllersFactory.createArduinoController(type, device, index));
            ++index;
        }
        device.controllers = controllers;

        return RaspberrySmartHome.getInstance().addDevice(device);
    }
}
