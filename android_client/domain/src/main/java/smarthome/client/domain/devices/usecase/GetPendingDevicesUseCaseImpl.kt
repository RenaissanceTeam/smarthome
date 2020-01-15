package smarthome.client.domain.devices.usecase

import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.domain.api.devices.usecase.GetPendingDevicesUseCase

class GetPendingDevicesUseCaseImpl(
    private val repo: DevicesRepo
) : GetPendingDevicesUseCase {
    override suspend fun execute(): List<GeneralDeviceInfo> {
        return repo.getPending()
    }
}