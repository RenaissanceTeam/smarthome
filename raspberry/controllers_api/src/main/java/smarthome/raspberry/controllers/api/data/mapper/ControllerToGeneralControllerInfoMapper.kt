package smarthome.raspberry.controllers.api.data.mapper

import smarthome.raspberry.controllers.api.data.dto.GeneralControllerInfo
import smarthome.raspberry.entity.Controller

interface ControllerToGeneralControllerInfoMapper {
    fun map(controller: Controller): GeneralControllerInfo
}