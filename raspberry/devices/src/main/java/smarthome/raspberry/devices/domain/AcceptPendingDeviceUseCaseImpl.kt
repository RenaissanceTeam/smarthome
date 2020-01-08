package smarthome.raspberry.devices.domain

import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.api.domain.AcceptPendingDeviceUseCase
import smarthome.raspberry.devices.data.DevicesRepository

class AcceptPendingDeviceUseCaseImpl(
        private val repository: DevicesRepository
) : AcceptPendingDeviceUseCase {
    override suspend fun execute(device: IotDevice) {
      TODO()
    }
}