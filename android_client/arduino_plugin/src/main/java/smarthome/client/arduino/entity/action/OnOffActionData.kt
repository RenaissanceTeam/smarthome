package smarthome.client.arduino.entity.action

import smarthome.client.entity.script.dependency.action.ActionData

data class OnOffActionData(
    val value: String = on
) : ActionData