package smarthome.raspberry.devices.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.api.domain.GetDevicesUseCase
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.devices.data.mapper.DeviceToGeneralDeviceInfoMapper
import smarthome.raspberry.entity.Device

@Component
open class GetDevicesUseCaseImpl(
        private val repository: DevicesRepository
) : GetDevicesUseCase {
    override fun execute(): List<Device> = repository.findAll()
}