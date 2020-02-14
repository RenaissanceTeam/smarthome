package smarthome.client.arduino.entity.action

import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.action.Action

data class OnOffAction(
    val value: String = "on",
    override val dependencyId: DependencyId
) : Action