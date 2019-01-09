package raspberry.smarthome.model.device.requests;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ArduinoDeviceAPI {

    @GET("service")
    Call<ControllerResponse> controllerReadRequest(@Query("index") int serviceIndex);

    @POST("service")
    Call<ControllerResponse> controllerWriteRequest(
            @Query("index") int serviceIndex, @Query("value") String value);

}
