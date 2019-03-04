package ru.smarthome.arduinodevices.controllers;

import java.io.IOException;

import ru.smarthome.arduinodevices.ArduinoDevice;
import ru.smarthome.arduinodevices.ArduinoControllerResponse;
import ru.smarthome.library.ControllerType;

public class ArduinoOnOff extends ArduinoController implements ArduinoWritable, ArduinoReadable {

    public static final String TAG = ArduinoOnOff.class.getSimpleName();

    public ArduinoOnOff(ArduinoDevice device, ControllerType type,
                        int indexInArduinoServicesArray) {
        super(device, type, indexInArduinoServicesArray);
    }

    @Override
    public ArduinoControllerResponse write(String value) throws IOException {
        return baseWrite(value);
    }

    @Override
    public ArduinoControllerResponse read() throws IOException {
        return baseRead();
    }
}
