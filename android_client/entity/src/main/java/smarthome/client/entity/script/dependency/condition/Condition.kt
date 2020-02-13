package smarthome.client.entity.script.dependency.condition

import smarthome.client.entity.script.dependency.DependencyUnit

data class Condition(
    override val id: DependencyUnitId,
    override val data: ConditionData
) : DependencyUnit