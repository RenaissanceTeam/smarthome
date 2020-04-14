package smarthome.raspberry.devices.domain.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.api.domain.dto.DeviceDTO
import smarthome.raspberry.entity.device.Device

@Component
open class DeviceDtoToDeviceMapper(
    private val controllerMapper: ControllerDtoToControllerMapper
) {
    fun map(dto: DeviceDTO): Device {
        return Device(
                serial = dto.serial,
                type = dto.type,
                controllers = mutableListOf(),
                description = dto.description,
                name = dto.name
        ).apply {
            dto.controllers.map { (controllers as MutableList).add(controllerMapper.map(this, it)) }
        }
    }
}

