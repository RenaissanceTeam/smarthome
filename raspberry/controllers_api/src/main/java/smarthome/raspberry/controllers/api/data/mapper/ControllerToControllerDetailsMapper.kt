package smarthome.raspberry.controllers.api.data.mapper

import smarthome.raspberry.controllers.api.data.dto.ControllerDetails
import smarthome.raspberry.entity.controller.Controller

interface ControllerToControllerDetailsMapper {
    fun map(controller: Controller): ControllerDetails
}