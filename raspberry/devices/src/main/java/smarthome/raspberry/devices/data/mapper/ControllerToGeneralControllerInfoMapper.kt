package smarthome.raspberry.devices.data.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.data.dto.GeneralControllerInfo
import smarthome.raspberry.entity.Controller

@Component
open class ControllerToGeneralControllerInfoMapper {

    fun map(controller: Controller) = GeneralControllerInfo(
            id = controller.id,
            deviceId = controller.device.id,
            type = controller.type,
            name = controller.name,
            state = controller.state
    )
}