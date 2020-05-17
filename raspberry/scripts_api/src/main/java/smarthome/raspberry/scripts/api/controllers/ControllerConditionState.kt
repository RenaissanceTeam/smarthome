package smarthome.raspberry.scripts.api.controllers

import smarthome.raspberry.entity.controller.Controller
import smarthome.raspberry.scripts.api.domain.ConditionState

class ControllerConditionState(
        val controller: Controller
) : ConditionState()