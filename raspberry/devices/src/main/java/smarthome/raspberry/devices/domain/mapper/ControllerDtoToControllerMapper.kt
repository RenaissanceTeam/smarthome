package smarthome.raspberry.devices.domain.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.api.domain.dto.ControllerDTO
import smarthome.raspberry.devices.domain.entity.Controller

@Component
open class ControllerDtoToControllerMapper {
    fun map(deviceSerialName: String, dto: ControllerDTO): Controller =
        Controller(
            deviceName = deviceSerialName,
            type = dto.type,
            name = dto.name
        )
}