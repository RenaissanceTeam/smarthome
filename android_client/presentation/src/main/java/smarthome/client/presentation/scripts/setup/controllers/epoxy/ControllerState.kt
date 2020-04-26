package smarthome.client.presentation.scripts.setup.controllers.epoxy

import smarthome.client.entity.Controller
import smarthome.client.entity.script.controller.ControllerBlock
import smarthome.client.util.DataStatus

data class ControllerState(
    val controller: DataStatus<Controller>,
    val block: ControllerBlock?,
    val visible: Boolean = true
)