package smarthome.client.data.devices

import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.data.devices.mapper.DeviceDetailsToDeviceMapper
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.entity.Device

class DevicesRepoImpl(
    private val retrofitFactory: RetrofitFactory,
    private val detailsToDeviceMapper: DeviceDetailsToDeviceMapper
) : DevicesRepo {
    
    override suspend fun getAdded(): List<GeneralDeviceInfo> {
        return retrofitFactory.createApi(DevicesApi::class.java).getAdded()
    }
    
    override suspend fun getById(deviceId: Long): Device {
        val details =  retrofitFactory.createApi(DevicesApi::class.java)
            .getDeviceDetails(deviceId)
            
        return detailsToDeviceMapper.map(details)
    }
    
    override suspend fun getPending(): List<GeneralDeviceInfo> {
        return retrofitFactory.createApi(DevicesApi::class.java).getPending()
    }
    
    override suspend fun acceptPending(id: Long) {
        retrofitFactory.createApi(DevicesApi::class.java).accept(id)
    }
    
    override suspend fun declinePending(id: Long) {
        retrofitFactory.createApi(DevicesApi::class.java).decline(id)
    }
}