package smarthome.raspberry.devices.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.api.domain.GetDeviceBySerialUseCase
import smarthome.raspberry.devices.data.DevicesRepository

@Component
class GetDeviceBySerialUseCaseImpl(
        private val repo: DevicesRepository
) : GetDeviceBySerialUseCase {
    override fun execute(serial: Int) = repo.findBySerial(serial)
}