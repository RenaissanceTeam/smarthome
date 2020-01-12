package smarthome.client.data.controllers

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import smarthome.client.entity.Controller

interface ControllersApi {
    
    @GET("api/controllers/{id}")
    suspend fun getDetails(@Path("id") id: Long): Controller
    
    @GET("api/controllers/{id}/read")
    suspend fun readState(@Path("id") id: Long): String
    
    @POST("api/controllers/{id}/write")
    suspend fun changeState(@Path("id") id: Long, @Body state: String): String
}