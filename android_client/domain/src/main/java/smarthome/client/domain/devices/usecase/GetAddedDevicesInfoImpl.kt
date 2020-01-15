package smarthome.client.domain.devices.usecase

import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.domain.api.devices.usecase.GetGeneralDevicesInfo

class GetAddedDevicesInfoImpl(
    private val repo: DevicesRepo
) : GetGeneralDevicesInfo {
    override suspend fun execute(): List<GeneralDeviceInfo> {
        return repo.getAdded()
    }
}