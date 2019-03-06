package smarthome.raspberry.server;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import smarthome.raspberry.model.RaspberrySmartHome;
import smarthome.raspberry.arduinodevices.ArduinoDevice;
import smarthome.raspberry.arduinodevices.controllers.ArduinoControllersFactory;
import smarthome.raspberry.arduinodevices.controllers.ArduinoReadable;
import smarthome.raspberry.arduinodevices.controllers.ArduinoWritable;
import smarthome.raspberry.arduinodevices.ArduinoControllerResponse;
import smarthome.library.common.BaseController;
import smarthome.library.common.ControllerType;

public class WebServer extends NanoHTTPD implements StoppableServer{

    public static final String TAG = WebServer.class.getSimpleName();

    public WebServer() {
        super(8080);
    }


    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        Log.d(TAG, "serve: " + method + ":" + uri + " " + session.getQueryParameterString());

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
                return new Response("ArduinoDevice was not added");
            }

            if (uri.startsWith("/reset")) {
                RaspberrySmartHome.getInstance().removeAll();
                return new Response("Everything is deleted");
            }

            if (uri.startsWith("/controller")) {
                return makeWriteRequestToDevice(session);
            }

            if (uri.startsWith("/alert")) {
                try {
                    serveAlertRequest(session);
                    return new Response("alert ok");
                } catch (Exception e) {
                    return new Response("alert exception " + e);
                }
            }
        }

        return getInvalidRequestResponse("No suitable method found");
    }

    private void serveAlertRequest(IHTTPSession session) throws NumberFormatException{
        Map<String, String> params = session.getParms();
        int index = Integer.parseInt(params.get("ind"));
        String value = params.get("value");
        String ip = session.getHeaders().get("http-client-ip");


        ArduinoDevice arduino = RaspberrySmartHome.getInstance().getArduinoByIp(ip);
        BaseController controller = arduino.controllers.get(index);
        controller.state = value;
        Log.d(TAG, "alert! new state=" + value + "for controller " + controller);
        // todo save to firestore (notify android client, send FCM)
    }

    @NonNull
    private Response getInvalidRequestResponse(String message) {
        return new Response(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Invalid request: " + message);
    }

    @NonNull
    private Response makeReadRequestToDevice(IHTTPSession session) {
        Map<String, String> params = session.getParms();
        BaseController controller = getController(params);
        if (controller instanceof ArduinoReadable) {
            try {
                ArduinoControllerResponse response = ((ArduinoReadable) controller).read();
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

            if (controller instanceof ArduinoWritable) {
                String value = params.get("value");
                ArduinoControllerResponse response = ((ArduinoWritable) controller).write(value);
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
        long controllerGuid = Long.parseLong(params.get("controller_guid"));
        return RaspberrySmartHome.getInstance().getController(controllerGuid);
    }

    private boolean initNewArduinoDevice(IHTTPSession session) {
        Map<String, String> params = session.getParms();
        String name = params.get("name");
        String description = params.get("description");
        String ip = session.getHeaders().get("http-client-ip");

        ArduinoDevice device = new ArduinoDevice(name, description, ip);

        String[] rawServices = params.get("services").split(";");
        List<BaseController> controllers = new ArrayList<>();
        int index = 0;
        for (String rawService : rawServices) {
            int id = Integer.parseInt(rawService.trim());
            ControllerType type = ControllerType.getById(id);
            controllers.add(ArduinoControllersFactory.createArduinoController(type, device, index));
            ++index;
        }
        device.controllers = controllers;

        return RaspberrySmartHome.getInstance().addDevice(device);
    }

    @Override
    public void startServer() {
        try {
            start();
        } catch (IOException e) {
            Log.e(TAG, "startServer: ", e);
        }
    }

    @Override
    public void stopServer() {
        stop();
    }
}
