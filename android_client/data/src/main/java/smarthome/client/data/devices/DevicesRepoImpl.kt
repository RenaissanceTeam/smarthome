package smarthome.client.data.devices

import kotlinx.coroutines.delay
import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.entity.Controller
import smarthome.client.entity.Device

class DevicesRepoImpl(
    private val retrofitFactory: RetrofitFactory
) : DevicesRepo {
    
    override suspend fun getAdded(): List<GeneralDeviceInfo> {
        return retrofitFactory.createApi(DevicesApi::class.java).getAdded()
    }
    
    override suspend fun getById(deviceId: Long): Device {
        return retrofitFactory.createApi(DevicesApi::class.java)
            .getDeviceDetails(deviceId)
    }
    
    override suspend fun getPending(): List<GeneralDeviceInfo> {
        return (1..40).map {
            GeneralDeviceInfo(
                it.toLong(),
                "name $it",
                "arduino",
                (1..5).map { c ->
                    Controller(
                        c.toLong(),
                        it.toLong(),
                        "dev $it - name $c",
                        listOf("dht", "onoff")[(0..1).random()],
                        "state $c"
                    )
                }
            )
        }
        
        //return retrofitFactory.createApi(DevicesApi::class.java).getPending()
    }
}