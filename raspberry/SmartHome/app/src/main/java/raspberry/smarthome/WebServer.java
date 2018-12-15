package raspberry.smarthome;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import raspberry.smarthome.model.DevicesStorage;
import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.controllers.BaseController;
import raspberry.smarthome.model.device.controllers.ControllerTypes;

public class WebServer extends NanoHTTPD {

    public static final String TAG = WebServer.class.getSimpleName();

    public WebServer() {
        super(8080);
    }


    @Override
    public Response serve(IHTTPSession session) {
        Log.d(TAG, "serve: " + session);
        Method method = session.getMethod();
        String uri = session.getUri();

        if (method == Method.GET) {
            if (uri.startsWith("/controller")) {
                // todo implement get request for android client
            }
            // todo implement basic web interface with info about current controllers state
        } else if (method == Method.POST) {
            if (uri.startsWith("/init")) {
                // todo add check if it's really arduino if other devices will be added the same way
                initNewArduinoDevice(session);
            } else if (uri.startsWith("/controller")) {
                // todo process write request to controller (make http request to arduino server
            } else if (uri.startsWith("/alert")) {
                // todo implement post request for arduino controllers (notify android client about
                // some state change)
            }
        }

        return new Response("ok"); // todo change to 404 error, because can't process unknown req
    }

    private void initNewArduinoDevice(IHTTPSession session) {
        Map<String, String> params = session.getParms();
        String name = params.get("name");
        String description = params.get("description");
        String ip = session.getHeaders().get("http-client-ip");

        ArduinoIotDevice device = new ArduinoIotDevice(name, description, ip);

        String[] rawServices = params.get("services").split(",");
        List<BaseController> controllers = new ArrayList<>();
        int index = 0;
        for (String rawService : rawServices) {
            int id = Integer.parseInt(rawService.trim());
            controllers.add(ControllerTypes.getById(id).createArduinoController(device, index));
            ++index;
        }
        device.controllers = controllers;

        DevicesStorage.getInstance().addDevice(device);
    }
}
