package smarthome.raspberry.devices_api.domain

import smarthome.library.common.Id
import smarthome.library.common.IotDevice

interface GetDeviceByIdUseCase {
    suspend fun execute(id: Id): IotDevice
}