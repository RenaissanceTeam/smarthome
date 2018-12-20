package raspberry.smarthome.model.device.controllers;

import java.io.IOException;

import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.requests.ArduinoDeviceAPI;
import raspberry.smarthome.model.device.requests.ControllerResponse;
import retrofit2.Call;

public class ArduinoOnOff extends ArduinoController implements Writable, Readable {

    public static final String TAG = ArduinoOnOff.class.getSimpleName();

    public ArduinoOnOff(ArduinoIotDevice device, ControllerTypes type,
                        int indexInArduinoServicesArray) {
        super(device, type, indexInArduinoServicesArray);
    }

    @Override
    public ControllerResponse write(String value) throws IOException {
        ArduinoDeviceAPI arduinoApi = getArduinoDeviceAPI();
        Call<ControllerResponse> call = arduinoApi.controllerWriteRequest(indexInArduinoServicesArray, value);

        ControllerResponse controllerResponse = call.execute().body();
        setNewState(controllerResponse.response);
        return controllerResponse;
    }

    @Override
    public ControllerResponse read() throws IOException {
        ArduinoDeviceAPI arduinoApi = getArduinoDeviceAPI();
        Call<ControllerResponse> call = arduinoApi.controllerReadRequest(indexInArduinoServicesArray);

        ControllerResponse controllerResponse = call.execute().body();
        setNewState(controllerResponse.response);
        return controllerResponse;
    }
}
