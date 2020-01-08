package smarthome.raspberry.devices.domain

import smarthome.library.common.Id
import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.api.domain.GetDeviceByIdUseCase
import smarthome.raspberry.devices.data.DevicesRepository

class GetDeviceByIdUseCaseImpl(
        val repository: DevicesRepository
) : GetDeviceByIdUseCase {
    override suspend fun execute(id: Id): IotDevice {
        TODO()
    
    }
}