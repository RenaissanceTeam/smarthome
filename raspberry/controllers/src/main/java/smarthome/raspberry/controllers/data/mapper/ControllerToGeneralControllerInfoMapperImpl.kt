package smarthome.raspberry.controllers.data.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.controllers.api.data.dto.GeneralControllerInfo
import smarthome.raspberry.controllers.api.data.mapper.ControllerToGeneralControllerInfoMapper
import smarthome.raspberry.entity.controller.Controller

@Component
open class ControllerToGeneralControllerInfoMapperImpl: ControllerToGeneralControllerInfoMapper {
    override fun map(controller: Controller) = GeneralControllerInfo(
            id = controller.id,
            deviceId = controller.device.id,
            type = controller.type,
            name = controller.name,
            state = controller.state
    )
}