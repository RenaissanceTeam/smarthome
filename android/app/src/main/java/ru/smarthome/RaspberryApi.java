package ru.smarthome;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RaspberryApi {

    @GET("info")
    Call<SmartHome> getSmartHomeState();

    @GET("controller")
    Call<ControllerResponse> readControllerState(@Query("device_guid") long deviceGuid,
                                     @Query("controller_guid") long controllerGuid);

    @POST("controller")
    Call<ControllerResponse> changeControllerState(@Query("device_guid") long deviceGuid,
                                       @Query("controller_guid") long controllerGuid,
                                       @Query("value") String value);
}
