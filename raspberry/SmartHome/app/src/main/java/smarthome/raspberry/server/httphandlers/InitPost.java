package smarthome.raspberry.server.httphandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import smarthome.library.common.BaseController;
import smarthome.library.common.ControllerType;
import smarthome.raspberry.arduinodevices.ArduinoDevice;
import smarthome.raspberry.arduinodevices.controllers.ArduinoControllersFactory;
import smarthome.raspberry.model.RaspberrySmartHome;

public class InitPost extends BaseRequestHandler {
    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        // todo add check if it's really arduino if other devices will be added the same way
        if (initNewArduinoDevice(session)) {
            return new NanoHTTPD.Response("Added successfully");
        }
        return new NanoHTTPD.Response("ArduinoDevice was not added");
    }

    private boolean initNewArduinoDevice(NanoHTTPD.IHTTPSession session) {
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

}
