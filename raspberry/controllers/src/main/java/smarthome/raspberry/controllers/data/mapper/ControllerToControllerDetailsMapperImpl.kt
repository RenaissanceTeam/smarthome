package smarthome.raspberry.controllers.data.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.controllers.api.data.dto.ControllerDetails
import smarthome.raspberry.controllers.api.data.mapper.ControllerToControllerDetailsMapper
import smarthome.raspberry.entity.Controller

@Component
open class ControllerToControllerDetailsMapperImpl : ControllerToControllerDetailsMapper {
    override fun map(controller: Controller) = ControllerDetails(
            id = controller.id,
            deviceId = controller.device.id,
            type = controller.type,
            name = controller.name,
            state = controller.state
    )
}