package smarthome.client.domain.usecases

import io.reactivex.Observable
import smarthome.client.domain.HomeRepository
import smarthome.library.common.IotDevice

class DevicesUseCase(private val repository: HomeRepository) {

    suspend fun getDevices(): Observable<MutableList<IotDevice>>{
        return repository.getDevices()
    }

    suspend fun changeDevice(device: IotDevice) {
        repository.updateDevice(device)
    }

    suspend fun getDevice(deviceGuid: Long): IotDevice? {
        return repository.getDevice(deviceGuid)
    }
}

