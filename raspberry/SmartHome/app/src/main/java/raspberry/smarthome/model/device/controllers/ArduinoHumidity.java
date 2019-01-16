package raspberry.smarthome.model.device.controllers;

import java.io.IOException;

import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.requests.ControllerResponse;

public class ArduinoHumidity extends ArduinoController implements Readable{

    public ArduinoHumidity(ArduinoIotDevice device, ControllerTypes type, int indexInArduinoServicesArray) {
        super(device, type, indexInArduinoServicesArray);
    }

    @Override
    public ControllerResponse read() throws IOException {
        return baseRead();
    }
}