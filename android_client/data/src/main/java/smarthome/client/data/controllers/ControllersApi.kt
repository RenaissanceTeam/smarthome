package smarthome.client.data.controllers

import retrofit2.http.GET
import retrofit2.http.Path
import smarthome.client.entity.Controller

interface ControllersApi {
    
    @GET("api/controllers/{id}")
    suspend fun getDetails(@Path("id") id: Long): Controller
}