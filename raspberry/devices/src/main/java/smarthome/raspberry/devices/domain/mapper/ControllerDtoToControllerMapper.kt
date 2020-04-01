package smarthome.raspberry.devices.domain.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.api.domain.dto.ControllerDTO
import smarthome.raspberry.entity.controller.Controller
import smarthome.raspberry.entity.device.Device

@Component
open class ControllerDtoToControllerMapper {
    fun map(device: Device, dto: ControllerDTO): Controller =
            Controller(
                    device = device,
                    type = dto.type,
                    name = dto.name,
                    state = dto.state
            )
}