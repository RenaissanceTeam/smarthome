package smarthome.raspberry.devices.domain.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.api.domain.dto.DeviceDTO
import smarthome.raspberry.devices.domain.entity.Device


@Component
open class DeviceDtoToDeviceMapper(
    private val controllerMapper: ControllerDtoToControllerMapper
) {
    fun map(dto: DeviceDTO): Device = Device(
        serialName = dto.serialName,
        type = dto.type,
        controllers = dto.controllers.map { controllerMapper.map(dto.serialName, it) },
        description = dto.description,
        name = dto.name
    )
}

