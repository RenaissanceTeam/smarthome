package raspberry.smarthome.model.device.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.requests.ArduinoDeviceAPI;
import raspberry.smarthome.model.device.requests.ControllerResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        // todo check for null
        return call.execute().body();
    }

    @Override
    public ControllerResponse read() throws IOException {
        ArduinoDeviceAPI arduinoApi = getArduinoDeviceAPI();
        Call<ControllerResponse> call = arduinoApi.controllerReadRequest(indexInArduinoServicesArray);

        // todo check for null
        return call.execute().body();
    }

    private ArduinoDeviceAPI getArduinoDeviceAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        String baseUrl = "http://" + this.device.ip + ":8080/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(ArduinoDeviceAPI.class);
    }
}
