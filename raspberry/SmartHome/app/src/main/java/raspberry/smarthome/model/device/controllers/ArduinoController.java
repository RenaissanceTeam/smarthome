package raspberry.smarthome.model.device.controllers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import raspberry.smarthome.model.device.ArduinoIotDevice;
import raspberry.smarthome.model.device.requests.ArduinoDeviceAPI;
import raspberry.smarthome.model.device.requests.ControllerResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.smarthome.library.BaseController;
import ru.smarthome.library.ControllerType;
import ru.smarthome.library.GUID;

public class ArduinoController extends BaseController {
    public final ArduinoIotDevice device;
    public final int indexInArduinoServicesArray;


    public ArduinoController(ArduinoIotDevice device, ControllerType type, int indexInArduinoServicesArray) {
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
