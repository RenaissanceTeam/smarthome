package raspberry.smarthome.model.device;

import java.util.ArrayList;

import raspberry.smarthome.model.device.controllers.ControllerTypes;

public class ArduinoIotDevice extends IotDevice {

    public final String ip;
    public ArduinoIotDevice(String name, String description, String ip,
                            ControllerTypes... controllerTypes) {
        super(name);
        this.name = name;
        this.description = description;
        this.controllers = new ArrayList<>();
        this.ip = ip;

        for (int i = 0; i < controllerTypes.length; i++) {
            controllers.add(controllerTypes[i].createArduinoController(this, i));
        }
    }

    @Override
    public boolean connect() {
        return false;
    }

    @Override
    public boolean disconnect() {
        return false;
    }


    @Override
    public String toString() {
        return "ArduinoIotDevice{" +
                "ip='" + ip + '\'' +
                ", controllers=" + controllers +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", GUID=" + GUID +
                '}';
    }
}
