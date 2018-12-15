package raspberry.smarthome.model.device.controllers;

import raspberry.smarthome.model.device.ArduinoIotDevice;

public enum ControllerTypes {
    ARDUINO_READ_FLOAT(1000), ARDUINO_ON_OFF(1001), ARDUINO_DIMMER(1001);

    public final int id;

    ControllerTypes(int id) {
        this.id = id;
    }

    public static ControllerTypes getById(int id) {
        for (ControllerTypes type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("No such controller type=" + id);
    }

    public ArduinoController createArduinoController(ArduinoIotDevice device, int index) {
        switch (this) {
            case ARDUINO_READ_FLOAT: return new ArduinoReadFloat(device,this,  index);
            case ARDUINO_ON_OFF: return new ArduinoOnOff(device, this, index);
        }
        return null;
    }
}
