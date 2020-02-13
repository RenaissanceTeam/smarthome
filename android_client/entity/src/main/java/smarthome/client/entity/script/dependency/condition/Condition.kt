package smarthome.client.entity.script.dependency.condition

import smarthome.client.entity.script.dependency.DependencyUnit

data class Condition(val id: DependencyUnitId, val data: ConditionData) : DependencyUnit