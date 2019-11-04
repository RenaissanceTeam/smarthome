package smarthome.raspberry.devices.domain

import smarthome.library.common.Id
import smarthome.library.common.IotDevice
import smarthome.raspberry.devices_api.data.DevicesRepository
import smarthome.raspberry.devices_api.domain.GetDeviceByIdUseCase
import smarthome.raspberry.devices_api.domain.NoDeviceException

class GetDeviceByIdUseCaseImpl(
        val repository: DevicesRepository
) : GetDeviceByIdUseCase {
    override suspend fun execute(id: Id): IotDevice {
        val devices = repository.getCurrentDevices()

        return devices.find { it.id == id } ?: throw NoDeviceException()
    }
}