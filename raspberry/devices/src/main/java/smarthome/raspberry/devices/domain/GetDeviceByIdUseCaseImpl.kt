package smarthome.raspberry.devices.domain

import smarthome.library.common.Id
import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.devices.api.domain.GetDeviceByIdUseCase
import smarthome.raspberry.devices.api.domain.NoDeviceException

class GetDeviceByIdUseCaseImpl(
        val repository: DevicesRepository
) : GetDeviceByIdUseCase {
    override suspend fun execute(id: Id): IotDevice {
        val devices = repository.getCurrentDevices()

        return devices.find { it.id == id } ?: throw NoDeviceException()
    }
}