package smarthome.client.data.controllers

import retrofit2.http.*
import smarthome.client.data.controllers.dto.StateDto
import smarthome.client.entity.Controller

interface ControllersApi {
    
    @GET("api/controllers/{id}")
    suspend fun getDetails(@Path("id") id: Long): Controller
    
    @GET("api/controllers/{id}/read")
    suspend fun readState(@Path("id") id: Long): StateDto
    
    @POST("api/controllers/{id}/write")
    suspend fun changeState(@Path("id") id: Long, @Body state: StateDto): StateDto

    @PATCH("api/controllers/{id]/name")
    suspend fun updateName(@Path("id") id: Long, @Body name: String)
}