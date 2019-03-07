package smarthome.raspberry.arduinodevices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import smarthome.library.common.BaseController;
import smarthome.library.common.IotDevice;
import smarthome.raspberry.arduinodevices.controllers.ArduinoController;

public class ArduinoDevice extends IotDevice {
    @Expose public final String ip;

    private final Gson gson;
    public ArduinoDevice(String name, String description, String ip) {
        super(name, description);
        this.ip = ip;
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
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
    public String gsonned() {
        return gson.toJson(this);
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
