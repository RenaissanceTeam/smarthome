package smarthome.client.data.devices

import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo

class DevicesRepoImpl(
    private val retrofitFactory: RetrofitFactory
) : DevicesRepo {
    
    override suspend fun getGeneralInfo(): List<GeneralDeviceInfo> {
        return retrofitFactory.getInstance().create(DevicesApi::class.java)
            .getGeneralInfo()
    }
}