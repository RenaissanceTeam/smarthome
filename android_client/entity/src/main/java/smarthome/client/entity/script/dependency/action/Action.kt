package smarthome.client.entity.script.dependency.action

import smarthome.client.entity.script.dependency.DependencyUnit

data class Action(
    val id: ActionId,
    val data: ActionData
): DependencyUnit
