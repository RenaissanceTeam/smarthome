package ru.smarthome.network.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.smarthome.network.model.RaspberryInfo;
import ru.smarthome.network.model.User;

public interface Client {

    @POST("post/update_user")
    Call<Void> updateUser(@Body User user);

    @POST("post/create_user")
    Call<Long> createUser(@Body User user);

    @POST("post/update_raspberry_info/{id}")
    Call<Void> updateRaspberryInfo(@Path("id") long userId,
                                   @Body RaspberryInfo info);

    @GET("get/user/{email}")
    Call<User> getUser(@Path("email") String email);

    @GET("get/user/{id}")
    Call<User> getUserById(@Path("id") long userId);

    @GET("get/user/{id}")
    Call<RaspberryInfo> getRaspberryInfo(@Path("id") long userId);

    @DELETE("delete/user/{id}")
    Call<Void> deleteUser(@Path("id") long userId);

}
