package ru.smarthome.arduinodevices.controllers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.smarthome.arduinodevices.ArduinoDevice;
import ru.smarthome.arduinodevices.ArduinoControllerResponse;
import ru.smarthome.arduinodevices.ArduinoDeviceAPI;
import ru.smarthome.library.BaseController;
import ru.smarthome.library.ControllerType;
import ru.smarthome.library.GUID;

public class ArduinoController extends BaseController {
    public final ArduinoDevice device;
    public final int indexInArduinoServicesArray;

    public ArduinoController(ArduinoDevice device, ControllerType type, int indexInArduinoServicesArray) {
        this.device = device;
        this.type = type;
        this.indexInArduinoServicesArray = indexInArduinoServicesArray;
        this.guid = GUID.getInstance().generateGuidForController(this);
    }

    @Override
    public synchronized void setNewState(String newState) {
        state = newState;
        Log.d(this.getClass().getSimpleName(), "new state: " + newState);
        // todo notify all android clients about new state (if it's changed)
    }

    public ArduinoControllerResponse baseRead() throws IOException {
        Call<ArduinoControllerResponse> call =
                getArduinoDeviceAPI().controllerReadRequest(indexInArduinoServicesArray);

        ArduinoControllerResponse controllerResponse = call.execute().body(); // synchronous
        if (controllerResponse != null) setNewState(controllerResponse.response);
        return controllerResponse;
    }

    public ArduinoControllerResponse baseWrite(String value) throws IOException{
        Call<ArduinoControllerResponse> call =
                getArduinoDeviceAPI().controllerWriteRequest(indexInArduinoServicesArray, value);

        ArduinoControllerResponse controllerResponse = call.execute().body(); // synchronous
        if (controllerResponse != null) setNewState(controllerResponse.response);
        return controllerResponse;
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

    @Override
    public String toString() {
        return "ArduinoController{" +
                "device guid=" + device.guid +
                ", guid=" + guid +
                ", indexInArduinoServicesArray=" + indexInArduinoServicesArray +
                ", type=" + type +
                '}';
    }
}