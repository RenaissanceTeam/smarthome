package smarthome.client.data.scripts

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import smarthome.client.entity.script.Script
import smarthome.client.entity.script.ScriptOverview

interface ScriptsApi {

    @GET("api/scripts")
    suspend fun fetchAll(): List<ScriptOverview>

    @GET("api/scripts/{id}")
    suspend fun fetchDetails(@Path("id") id: Long): Script

    @POST("api/scripts")
    suspend fun save(@Body script: Script): Script
}