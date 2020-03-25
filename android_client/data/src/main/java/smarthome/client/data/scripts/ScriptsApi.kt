package smarthome.client.data.scripts

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import smarthome.client.entity.script.Script

interface ScriptsApi {
    
    @GET("api/scripts")
    suspend fun fetchAll(): List<Script>
    
    @POST("api/scripts")
    suspend fun save(@Body script: Script): Script
}