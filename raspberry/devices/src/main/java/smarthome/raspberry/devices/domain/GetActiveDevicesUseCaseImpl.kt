package smarthome.raspberry.devices.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.api.domain.GetActiveDevicesUseCase
import smarthome.raspberry.devices.data.DeviceStatusRepository
import smarthome.raspberry.entity.device.Device
import smarthome.raspberry.entity.device.DeviceStatuses

@Component
open class GetActiveDevicesUseCaseImpl(
        private val statusRepository: DeviceStatusRepository
) : GetActiveDevicesUseCase {
    override fun execute(): List<Device> = statusRepository.findByStatus(DeviceStatuses.ACCEPTED.name).map { it.device }
}