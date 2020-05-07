package smarthome.raspberry.devices.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.api.domain.GetDeviceByIdUseCase
import smarthome.raspberry.devices.api.domain.UpdateDeviceUseCase
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.entity.device.Device

@Component
class UpdateDeviceUseCaseImpl(
        private val getDeviceByIdUseCase: GetDeviceByIdUseCase,
        private val devicesRepository: DevicesRepository
) : UpdateDeviceUseCase {
    override fun execute(id: Long, partialUpdate: (Device) -> Device): Device {
        val device = getDeviceByIdUseCase.execute(id)

        return devicesRepository.save(partialUpdate(device))
    }
}