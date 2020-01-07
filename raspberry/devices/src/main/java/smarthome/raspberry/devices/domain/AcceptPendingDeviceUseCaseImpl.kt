package smarthome.raspberry.devices.domain

import smarthome.raspberry.devices.api.domain.AcceptPendingDeviceUseCase
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.entity.Device

class AcceptPendingDeviceUseCaseImpl(
        private val repository: DevicesRepository
) : AcceptPendingDeviceUseCase {
    override suspend fun execute(device: Device) {
      TODO()
    }
}