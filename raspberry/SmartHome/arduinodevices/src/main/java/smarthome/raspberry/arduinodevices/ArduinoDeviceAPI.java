//package smarthome.raspberry.arduinodevices;
//
//import retrofit2.Call;
//import retrofit2.http.GET;
//import retrofit2.http.POST;
//import retrofit2.http.Query;
//
//public interface ArduinoDeviceAPI {
//
//    @GET("service")
//    Call<ArduinoControllerResponse> controllerReadRequest(@Query("index") int serviceIndex);
//
//    @POST("service")
//    Call<ArduinoControllerResponse> controllerWriteRequest(
//            @Query("index") int serviceIndex, @Query("value") String value);
//
//}
