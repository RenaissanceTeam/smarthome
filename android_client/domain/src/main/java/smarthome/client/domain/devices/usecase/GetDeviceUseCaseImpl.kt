package smarthome.client.domain.devices.usecase

import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.domain.api.devices.usecase.GetDeviceUseCase
import smarthome.client.entity.Device

class GetDeviceUseCaseImpl(
    private val repo: DevicesRepo
) : GetDeviceUseCase {
    override suspend fun execute(deviceId: Long): Device {
        return repo.getById(deviceId)
    }
}