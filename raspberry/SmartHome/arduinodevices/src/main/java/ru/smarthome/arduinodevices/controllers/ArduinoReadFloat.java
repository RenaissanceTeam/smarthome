package ru.smarthome.arduinodevices.controllers;

import java.io.IOException;

import retrofit2.Call;
import ru.smarthome.arduinodevices.ArduinoControllerResponse;
import ru.smarthome.arduinodevices.ArduinoDevice;
import ru.smarthome.arduinodevices.ArduinoDeviceAPI;
import ru.smarthome.library.ControllerType;

public class ArduinoReadFloat extends ArduinoController implements ArduinoReadable {

    public ArduinoReadFloat(ArduinoDevice device, ControllerType type,
                            int indexInArduinoServicesArray) {
        super(device, type, indexInArduinoServicesArray);
    }

    @Override
    public ArduinoControllerResponse read() throws IOException {
        ArduinoDeviceAPI arduinoApi = getArduinoDeviceAPI();
        Call<ArduinoControllerResponse> call = arduinoApi.controllerReadRequest(indexInArduinoServicesArray);

        ArduinoControllerResponse controllerResponse = call.execute().body();
        if (controllerResponse != null) {
            String response = controllerResponse.response;
            double val = Double.parseDouble(response);
            controllerResponse.response = ((int)(val * 100 / 1025)) + " %";
            setNewState(controllerResponse.response);
        }
        return controllerResponse;
    }
}
