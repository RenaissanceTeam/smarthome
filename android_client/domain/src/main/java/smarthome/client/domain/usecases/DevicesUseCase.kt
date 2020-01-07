package smarthome.client.domain.usecases

import io.reactivex.Observable
import smarthome.client.data_api.home.HomeRepository
import smarthome.client.domain_api.entity.Device

class DevicesUseCase(private val repository: HomeRepository) {

    suspend fun getDevices(): Observable<MutableList<Device>>{
        return repository.getDevices()
    }

    suspend fun changeDevice(device: Device) {
        TODO()
    }

    suspend fun getDevice(deviceGuid: Long): Device? {
        return repository.getDevice(deviceGuid)
    }
}

