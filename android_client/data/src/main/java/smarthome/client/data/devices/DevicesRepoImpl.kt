package smarthome.client.data.devices

import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.entity.Device

class DevicesRepoImpl(
    private val retrofitFactory: RetrofitFactory
) : DevicesRepo {
    
    override suspend fun getGeneralInfo(): List<GeneralDeviceInfo> {
        return retrofitFactory.createApi(DevicesApi::class.java)
            .getGeneralInfo()
    }
    
    override suspend fun getById(deviceId: Long): Device {
        return retrofitFactory.createApi(DevicesApi::class.java)
            .getDeviceDetails(deviceId)
    }
}