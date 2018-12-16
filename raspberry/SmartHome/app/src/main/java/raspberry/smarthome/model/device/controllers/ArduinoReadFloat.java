package raspberry.smarthome.model.device.controllers;

import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.requests.ControllerResponse;

public class ArduinoReadFloat extends ArduinoController implements Readable {

    public ArduinoReadFloat(ArduinoIotDevice device, ControllerTypes type,
                            int indexInArduinoServicesArray) {
        super(device, type, indexInArduinoServicesArray);
    }

    @Override
    public ControllerResponse read() {
        ControllerResponse response = new ControllerResponse();
        response.response = "123.1";
        return response; // todo http request here
    }
}
