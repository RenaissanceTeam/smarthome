package ru.smarthome.arduinodevices.controllers;

import java.io.IOException;

import ru.smarthome.arduinodevices.ArduinoControllerResponse;
import ru.smarthome.arduinodevices.ArduinoDevice;
import ru.smarthome.library.ControllerType;

public class ArduinoTemperature extends ArduinoController implements ArduinoReadable {

    public ArduinoTemperature(ArduinoDevice device, ControllerType type,
                              int indexInArduinoServicesArray) {
        super(device, type, indexInArduinoServicesArray);
    }

    @Override
    public ArduinoControllerResponse read() throws IOException {
        return baseRead();
    }
}
