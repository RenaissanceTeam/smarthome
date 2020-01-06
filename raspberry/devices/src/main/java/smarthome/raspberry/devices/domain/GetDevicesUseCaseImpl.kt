package smarthome.raspberry.devices.domain

import smarthome.raspberry.devices.api.domain.GetDevicesUseCase
import smarthome.raspberry.devices.data.DevicesRepository

class GetDevicesUseCaseImpl(private val repository: DevicesRepository): GetDevicesUseCase {
    override suspend fun execute() = TODO()
}