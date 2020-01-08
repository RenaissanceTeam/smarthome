package smarthome.client.domain.usecases

import io.reactivex.Observable
import smarthome.client.data.api.home.HomeRepository
import smarthome.client.domain.api.entity.Device
import smarthome.client.domain.api.usecase.DevicesUseCase

class DevicesUseCaseImpl(private val repository: HomeRepository) : DevicesUseCase {

    override suspend fun getDevices(): Observable<MutableList<Device>>{
        return repository.getDevices()
    }

    override suspend fun changeDevice(device: Device) {
        TODO()
    }

    override suspend fun getDevice(deviceGuid: Long): Device? {
        return repository.getDevice(deviceGuid)
    }
}

