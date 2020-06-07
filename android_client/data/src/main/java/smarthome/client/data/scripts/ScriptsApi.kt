package smarthome.client.data.scripts

import retrofit2.http.*
import smarthome.client.entity.script.Script
import smarthome.client.entity.script.ScriptOverview

interface ScriptsApi {

    @GET("api/scripts")
    suspend fun fetchAll(): List<ScriptOverview>

    @GET("api/scripts/{id}")
    suspend fun fetchDetails(@Path("id") id: Long): Script

    @POST("api/scripts")
    suspend fun save(@Body script: Script): Script

    @DELETE("api/scripts/{id}")
    suspend fun remove(@Path("id") id: Long)

    @POST("api/scripts/{id}/enabled")
    suspend fun setEnabled(@Path("id") id: Long, @Body enabled: Boolean)
}