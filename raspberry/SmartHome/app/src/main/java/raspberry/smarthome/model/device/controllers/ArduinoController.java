package raspberry.smarthome.model.device.controllers;

import com.google.gson.annotations.Expose;

import raspberry.smarthome.model.GUID;
import raspberry.smarthome.model.device.ArduinoIotDevice;

public class ArduinoController implements BaseController {
    public final ArduinoIotDevice device;
    @Expose public final long guid;
    public final int indexInArduinoServicesArray;
    @Expose public final ControllerTypes type;

    public ArduinoController(ArduinoIotDevice device, ControllerTypes type, int indexInArduinoServicesArray) {
        this.device = device;
        this.type = type;
        this.indexInArduinoServicesArray = indexInArduinoServicesArray;
        this.guid = GUID.getInstance().getGuidForController(this);
    }

    public String getTopic() {
        return "iot/device/ip/" + device.ip;
    }

    @Override
    public String toString() {
        return "ArduinoController{" +
                "device guid=" + device.guid +
                ", guid=" + guid +
                ", indexInArduinoServicesArray=" + indexInArduinoServicesArray +
                ", type=" + type +
                '}';
    }
}
