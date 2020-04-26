package smarthome.raspberry.devices.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.api.domain.DeclinePendingDeviceUseCase
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.entity.device.Device

@Component
class DeclinePendingDeviceUseCaseImpl(
        private val devicesRepository: DevicesRepository
) : DeclinePendingDeviceUseCase {
    override fun execute(device: Device) {
        devicesRepository.delete(device)
    }
}