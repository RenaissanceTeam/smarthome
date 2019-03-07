package smarthome.raspberry.arduinodevices.controllers;
import smarthome.library.common.ControllerType;
import smarthome.raspberry.arduinodevices.ArduinoDevice;

public class ArduinoControllersFactory {

    public static ArduinoController createArduinoController(ControllerType type,
                                                            ArduinoDevice device,
                                                            int index) {
        switch (type) {
            case HUMIDITY: return new ArduinoHumidity(device, type, index);
            case TEMPERATURE: return new ArduinoTemperature(device, type, index);
            case ARDUINO_ANALOG: return new ArduinoReadFloat(device, type, index);
            case ARDUINO_ON_OFF: return new ArduinoOnOff(device, type, index);
            case DIGITAL_ALERT: return new ArduinoDigitalAlert(device, type, index);
        }

        return null;
    }
}
