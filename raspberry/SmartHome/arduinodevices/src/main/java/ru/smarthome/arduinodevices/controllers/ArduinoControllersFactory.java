package ru.smarthome.arduinodevices.controllers;
import ru.smarthome.arduinodevices.ArduinoDevice;
import ru.smarthome.library.ControllerType;

public class ArduinoControllersFactory {

    public static ArduinoController createArduinoController(ControllerType type,
                                                            ArduinoDevice device,
                                                            int index) {
        switch (type) {
            case HUMIDITY: return new ArduinoHumidity(device, type, index);
            case TEMPERATURE: return new ArduinoTemperature(device, type, index);
            case ARDUINO_ANALOG: return new ArduinoReadFloat(device, type, index);
            case ARDUINO_ON_OFF: return new ArduinoOnOff(device, type, index);
        }

        return null;
    }
}
