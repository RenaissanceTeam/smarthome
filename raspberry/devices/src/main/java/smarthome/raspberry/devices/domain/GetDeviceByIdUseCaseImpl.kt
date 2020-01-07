package smarthome.raspberry.devices.domain

import smarthome.raspberry.devices.api.domain.GetDeviceByIdUseCase
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.entity.Device

class GetDeviceByIdUseCaseImpl(
        val repository: DevicesRepository
) : GetDeviceByIdUseCase {
    override suspend fun execute(id: Long): Device {
        TODO()
    
    }
}