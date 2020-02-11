package smarthome.client.entity.script.dependency

import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.DependencyCondition

data class DependencyDetails(
    val dependency: Dependency,
    val condition: DependencyCondition,
    val action: Action
)