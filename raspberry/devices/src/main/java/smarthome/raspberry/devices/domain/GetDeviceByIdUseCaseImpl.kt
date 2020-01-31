package smarthome.raspberry.devices.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.api.domain.GetDeviceByIdUseCase
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.entity.Device

@Component
open class GetDeviceByIdUseCaseImpl(
        private val repository: DevicesRepository
) : GetDeviceByIdUseCase {
    override fun execute(id: Long): Device {
        return repository.findById(id).get()
    }
}