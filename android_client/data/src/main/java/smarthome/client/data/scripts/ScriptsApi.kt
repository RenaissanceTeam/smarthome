package smarthome.client.data.scripts

import retrofit2.http.GET
import smarthome.client.data.scripts.dto.ScriptDto

interface ScriptsApi {
    
    @GET("api/scripts")
    suspend fun fetchAll(): List<ScriptDto>
}