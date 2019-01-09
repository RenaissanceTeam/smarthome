package raspberry.smarthome.model.device.controllers;

import java.io.IOException;

import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.requests.ArduinoDeviceAPI;
import raspberry.smarthome.model.device.requests.ControllerResponse;
import retrofit2.Call;
import ru.smarthome.library.ControllerType;

public class ArduinoReadFloat extends ArduinoController implements Readable {

    public ArduinoReadFloat(ArduinoIotDevice device, ControllerType type,
                            int indexInArduinoServicesArray) {
        super(device, type, indexInArduinoServicesArray);
    }

    @Override
    public ControllerResponse read() throws IOException {
        ArduinoDeviceAPI arduinoApi = getArduinoDeviceAPI();
        Call<ControllerResponse> call = arduinoApi.controllerReadRequest(indexInArduinoServicesArray);

        ControllerResponse controllerResponse = call.execute().body();
        if (controllerResponse != null) {
            String response = controllerResponse.response;
            double val = Double.parseDouble(response);
            controllerResponse.response = ((int)(val * 100 / 1025)) + " %";
            setNewState(controllerResponse.response);
        }
        return controllerResponse;
    }
}
