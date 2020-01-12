package smarthome.client.data.devices

import retrofit2.http.GET
import retrofit2.http.Path
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.entity.Device

interface DevicesApi {
    @GET("api/devices")
    suspend fun getGeneralInfo(): List<GeneralDeviceInfo>
    
    @GET("api/devices/{id}")
    suspend fun getDeviceDetails(@Path("id") id: Long): Device
}