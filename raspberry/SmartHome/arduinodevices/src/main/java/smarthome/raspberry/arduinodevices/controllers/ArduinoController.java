package smarthome.raspberry.arduinodevices.controllers;

import android.util.Log;

import com.google.firebase.firestore.Exclude;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import smarthome.raspberry.arduinodevices.ArduinoControllerResponse;
import smarthome.raspberry.arduinodevices.ArduinoDevice;
import smarthome.raspberry.arduinodevices.ArduinoDeviceAPI;
import smarthome.library.common.BaseController;
import smarthome.library.common.ControllerType;
import smarthome.library.common.GUID;

public class ArduinoController extends BaseController {
    @Exclude public ArduinoDevice device;
    @Exclude @Expose public final int indexInArduinoServicesArray;

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

    @Exclude ArduinoDeviceAPI getArduinoDeviceAPI() {
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
