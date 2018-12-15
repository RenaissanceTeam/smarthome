package raspberry.smarthome.model.device.controllers;

import raspberry.smarthome.model.device.ArduinoIotDevice;

public class ArduinoReadFloat extends ArduinoController implements Readable<Float> {

    public ArduinoReadFloat(ArduinoIotDevice device, ControllerTypes type,
                            int indexInArduinoServicesArray) {
        super(device, type, indexInArduinoServicesArray);
    }

    @Override
    public Float read() {
        return 123.1f; // todo http request here
    }
}
