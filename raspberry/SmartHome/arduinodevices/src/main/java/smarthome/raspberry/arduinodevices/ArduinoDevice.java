package smarthome.raspberry.arduinodevices;

import smarthome.raspberry.arduinodevices.controllers.ArduinoController;
import ru.smarthome.library.BaseController;
import ru.smarthome.library.IotDevice;

public class ArduinoDevice extends IotDevice {
    public final String ip;

    public ArduinoDevice(String name, String description, String ip) {
        super(name, description);
        this.ip = ip;
    }

    public ArduinoController getControllerByGuid(long guid) {
        for (BaseController controller : controllers) {
            if (((ArduinoController) controller).guid == guid) {
                return (ArduinoController) controller;
            }
        }
        throw new IllegalArgumentException("No controller with guid=" + guid + " for iotDevice=" + this);
    }

    @Override
    public String toString() {
        return "ArduinoDevice{" +
                "ip='" + ip + '\'' +
                ", controllers=" + controllers +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", GUID=" + guid +
                '}';
    }
}
