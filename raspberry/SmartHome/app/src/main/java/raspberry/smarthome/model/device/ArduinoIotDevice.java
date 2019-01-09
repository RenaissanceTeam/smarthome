package raspberry.smarthome.model.device;

import raspberry.smarthome.model.device.controllers.ArduinoController;
import ru.smarthome.library.BaseController;
import ru.smarthome.library.IotDevice;

public class ArduinoIotDevice extends IotDevice {
    public final String ip;

    public ArduinoIotDevice(String name, String description, String ip) {
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
        return "ArduinoIotDevice{" +
                "ip='" + ip + '\'' +
                ", controllers=" + controllers +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", GUID=" + guid +
                '}';
    }
}
