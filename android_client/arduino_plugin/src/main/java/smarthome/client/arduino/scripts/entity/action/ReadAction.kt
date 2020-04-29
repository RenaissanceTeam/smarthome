package smarthome.client.arduino.scripts.entity.action

import smarthome.client.entity.script.dependency.action.Action

data class ReadAction(
        override val id: String
) : Action()