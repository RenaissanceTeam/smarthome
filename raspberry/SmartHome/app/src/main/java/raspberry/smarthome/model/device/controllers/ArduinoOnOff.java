package raspberry.smarthome.model.device.controllers;

import java.io.IOException;

import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.requests.ControllerResponse;
import ru.smarthome.library.ControllerType;

public class ArduinoOnOff extends ArduinoController implements Writable, Readable {

    public static final String TAG = ArduinoOnOff.class.getSimpleName();

    public ArduinoOnOff(ArduinoIotDevice device, ControllerType type,
                        int indexInArduinoServicesArray) {
        super(device, type, indexInArduinoServicesArray);
    }

    @Override
    public ControllerResponse write(String value) throws IOException {
        return baseWrite(value);
    }

    @Override
    public ControllerResponse read() throws IOException {
        return baseRead();
    }
}
