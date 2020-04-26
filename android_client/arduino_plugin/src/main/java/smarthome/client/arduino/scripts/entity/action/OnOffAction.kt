package smarthome.client.arduino.scripts.entity.action

import smarthome.client.entity.script.dependency.action.Action

data class OnOffAction(
    override val id: String,
    val value: String = on
) : Action()