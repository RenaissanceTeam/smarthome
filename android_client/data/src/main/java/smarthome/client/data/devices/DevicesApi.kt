package smarthome.client.data.devices

import retrofit2.http.GET
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo

interface DevicesApi {
    @GET("api/devices")
    suspend fun getGeneralInfo(): List<GeneralDeviceInfo>
}