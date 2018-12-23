package ru.smarthome;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.smarthome.library.RaspberryResponse;

public interface RaspberryApi {

    @GET("info")
    Call<SmartHome> getSmartHomeState();

    // TODO: 12/20/18 maybe read/write should return different responses? or create many read responses
    // e.g. for temperature, ON/OFF, light, etc..
    @GET("controller")
    Call<RaspberryResponse> readControllerState(@Query("device_guid") long deviceGuid,
                                                @Query("controller_guid") long controllerGuid);

    @POST("controller")
    Call<RaspberryResponse> changeControllerState(@Query("device_guid") long deviceGuid,
                                                  @Query("controller_guid") long controllerGuid,
                                                  @Query("value") String value);
}
