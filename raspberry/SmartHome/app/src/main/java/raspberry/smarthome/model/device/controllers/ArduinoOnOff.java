package raspberry.smarthome.model.device.controllers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.requests.ArduinoDeviceAPI;
import raspberry.smarthome.model.device.requests.ControllerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArduinoOnOff extends ArduinoController implements Writable, Callback<ControllerResponse> {

    public static final int TIMEOUT_RETRY = 1000;
    public static final String TAG = ArduinoOnOff.class.getSimpleName();

    public ArduinoOnOff(ArduinoIotDevice device, ControllerTypes type,
                        int indexInArduinoServicesArray) {
        super(device, type, indexInArduinoServicesArray);
    }

    @Override
    public void write(String value) {

        // todo make http request to this device web server
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + this.device.ip + ":8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ArduinoDeviceAPI arduinoApi = retrofit.create(ArduinoDeviceAPI.class);

        Call<ControllerResponse> call = arduinoApi.controllerWriteRequest(indexInArduinoServicesArray, value);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ControllerResponse> call, Response<ControllerResponse> response) {
        if(response.isSuccessful()) {
            ControllerResponse res = response.body();
            Log.d(TAG, "onResponse: " + res);

        }
    }

    @Override
    public void onFailure(Call<ControllerResponse> call, Throwable t) {

    }
}
