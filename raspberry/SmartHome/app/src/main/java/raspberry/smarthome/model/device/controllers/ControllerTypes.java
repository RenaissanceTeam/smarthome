package raspberry.smarthome.model.device.controllers;

import raspberry.smarthome.model.device.ArduinoIotDevice;

public enum ControllerTypes {
    ARDUINO_READ_FLOAT, ARDUINO_ON_OFF, ARDUINO_DIMMER;

    public BaseController createArduinoController(ArduinoIotDevice device, int id) {
        switch (this) {
            case ARDUINO_READ_FLOAT: return new ArduinoReadFloat(device, id);
            case ARDUINO_ON_OFF: return new ArduinoOnOff(device, id);
        }
        return null;
    }
}
