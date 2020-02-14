package smarthome.client.entity.script.dependency.action

import smarthome.client.entity.script.dependency.DependencyUnit
import smarthome.client.entity.script.dependency.condition.DependencyUnitId

data class Action(
    override val id: DependencyUnitId,
    override val data: ActionData
): DependencyUnit
