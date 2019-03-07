package smarthome.raspberry.arduinodevices.controllers;

import java.io.IOException;

import smarthome.library.common.ControllerType;
import smarthome.raspberry.arduinodevices.ArduinoControllerResponse;
import smarthome.raspberry.arduinodevices.ArduinoDevice;

public class ArduinoDigitalAlert extends ArduinoController implements ArduinoReadable {
    public ArduinoDigitalAlert(ArduinoDevice device, ControllerType type, int indexInArduinoServicesArray) {
        super(device, type, indexInArduinoServicesArray);
    }

    @Override
    public ArduinoControllerResponse read() throws IOException {
        return baseRead();
    }
}