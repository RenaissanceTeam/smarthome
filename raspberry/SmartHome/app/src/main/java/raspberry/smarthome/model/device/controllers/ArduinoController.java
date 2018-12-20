package raspberry.smarthome.model.device.controllers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.IOException;

import raspberry.smarthome.model.GUID;
import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.requests.ArduinoDeviceAPI;
import raspberry.smarthome.model.device.requests.ControllerResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArduinoController implements BaseController {
    public final ArduinoIotDevice device;
    @Expose public final long guid;
    public final int indexInArduinoServicesArray;
    @Expose public final ControllerTypes type;
    @Expose public String state;

    public ArduinoController(ArduinoIotDevice device, ControllerTypes type, int indexInArduinoServicesArray) {
        this.device = device;
        this.type = type;
        this.indexInArduinoServicesArray = indexInArduinoServicesArray;
        this.guid = GUID.getInstance().getGuidForController(this);
    }

    @Override
    public synchronized void setNewState(String newState) {
        state = newState;
        Log.d(this.getClass().getSimpleName(), "new state: " + newState);
        // todo notify all android clients about new state (if it's changed)
    }

    public ControllerResponse baseRead() throws IOException {
        ArduinoDeviceAPI arduinoApi = getArduinoDeviceAPI();
        Call<ControllerResponse> call = arduinoApi.controllerReadRequest(indexInArduinoServicesArray);

        ControllerResponse controllerResponse = call.execute().body();
        if (controllerResponse != null) setNewState(controllerResponse.response);
        return controllerResponse;
    }

    public ControllerResponse baseWrite(String value) throws IOException{
        ArduinoDeviceAPI arduinoApi = getArduinoDeviceAPI();
        Call<ControllerResponse> call = arduinoApi.controllerWriteRequest(indexInArduinoServicesArray, value);

        ControllerResponse controllerResponse = call.execute().body();
        if (controllerResponse != null) setNewState(controllerResponse.response);
        return controllerResponse;
    }

    @Override
    public String toString() {
        return "ArduinoController{" +
                "device guid=" + device.guid +
                ", guid=" + guid +
                ", indexInArduinoServicesArray=" + indexInArduinoServicesArray +
                ", type=" + type +
                '}';
    }


    ArduinoDeviceAPI getArduinoDeviceAPI() {
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
