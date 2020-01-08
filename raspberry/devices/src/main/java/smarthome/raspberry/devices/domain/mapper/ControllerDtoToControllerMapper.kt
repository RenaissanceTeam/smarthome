package smarthome.raspberry.devices.domain.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.api.domain.dto.ControllerDTO
import smarthome.raspberry.devices.domain.entity.Controller
import smarthome.raspberry.devices.domain.entity.Device

@Component
open class ControllerDtoToControllerMapper {
    fun map(device: Device, dto: ControllerDTO): Controller =
        Controller(
            device = device,
            type = dto.type,
            name = dto.name
        )
}