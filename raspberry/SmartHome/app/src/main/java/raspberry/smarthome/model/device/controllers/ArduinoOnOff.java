package raspberry.smarthome.model.device.controllers;

import raspberry.smarthome.model.device.ArduinoIotDevice;

public class ArduinoOnOff extends ArduinoController implements Writable {

    public static final int TIMEOUT_RETRY = 1000;

    public ArduinoOnOff(ArduinoIotDevice device, ControllerTypes type,
                        int indexInArduinoServicesArray) {
        super(device, type, indexInArduinoServicesArray);
    }

    @Override
    public void write(String value) {
        // TODO: 11/30/2018 message into strings
        String message = indexInArduinoServicesArray + ";" + value;
        // todo make http request to this device web server
    }
}
