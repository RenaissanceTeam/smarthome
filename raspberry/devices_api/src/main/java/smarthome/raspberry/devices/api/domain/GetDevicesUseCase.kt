package smarthome.raspberry.devices.api.domain

import smarthome.library.common.IotDevice

interface GetDevicesUseCase {
    suspend fun execute(): List<IotDevice>
}