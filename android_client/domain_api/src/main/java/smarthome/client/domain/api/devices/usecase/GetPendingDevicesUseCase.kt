package smarthome.client.domain.api.devices.usecase

import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo

interface GetPendingDevicesUseCase {
    suspend fun execute(): List<GeneralDeviceInfo>
}