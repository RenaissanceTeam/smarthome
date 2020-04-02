package smarthome.client.entity.script.dependency.action

import smarthome.client.entity.script.dependency.DependencyUnit
data class Action(
    override val id: String,
    override val data: ActionData
): DependencyUnit
