package smarthome.raspberry.devices.api.domain

import smarthome.library.common.IotDevice

interface SaveDeviceUseCase {
    suspend fun execute(device: IotDevice)
}