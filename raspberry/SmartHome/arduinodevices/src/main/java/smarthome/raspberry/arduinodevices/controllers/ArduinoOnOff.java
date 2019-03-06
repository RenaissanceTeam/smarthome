package smarthome.raspberry.arduinodevices.controllers;

import java.io.IOException;

import smarthome.raspberry.arduinodevices.ArduinoControllerResponse;
import smarthome.raspberry.arduinodevices.ArduinoDevice;
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
