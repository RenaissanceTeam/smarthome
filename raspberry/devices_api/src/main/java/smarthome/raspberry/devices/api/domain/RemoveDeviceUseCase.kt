package smarthome.raspberry.devices.api.domain

import smarthome.library.common.IotDevice

interface RemoveDeviceUseCase {
    suspend fun execute(device: IotDevice)
}