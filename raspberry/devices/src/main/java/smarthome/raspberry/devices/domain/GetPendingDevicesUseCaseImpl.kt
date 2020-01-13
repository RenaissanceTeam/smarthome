package smarthome.raspberry.devices.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.api.domain.GetPendingDevicesUseCase
import smarthome.raspberry.devices.data.DeviceStatusRepository
import smarthome.raspberry.entity.Device
import smarthome.raspberry.entity.DeviceStatuses

@Component
open class GetPendingDevicesUseCaseImpl(
        private val statusRepository: DeviceStatusRepository
) : GetPendingDevicesUseCase {
    override fun execute(): List<Device> = statusRepository.findByStatus(DeviceStatuses.PENDING.name).map { it.device }
}