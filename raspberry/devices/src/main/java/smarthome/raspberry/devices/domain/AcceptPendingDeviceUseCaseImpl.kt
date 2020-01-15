package smarthome.raspberry.devices.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.api.domain.AcceptPendingDeviceUseCase
import smarthome.raspberry.devices.data.DeviceStatusRepository
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.entity.Device
import smarthome.raspberry.entity.DeviceStatuses

@Component
class AcceptPendingDeviceUseCaseImpl(
        private val statusRepository: DeviceStatusRepository
) : AcceptPendingDeviceUseCase {
    override fun execute(device: Device) {
        val status = statusRepository.findByDevice(device) ?: throw Throwable("no status for device ${device.id}")
        statusRepository.save(status.copy(status=DeviceStatuses.ACCEPTED.name))
    }
}


