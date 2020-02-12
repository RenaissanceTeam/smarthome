package smarthome.client.entity.script.dependency

import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition

data class DependencyDetails(
    val dependency: Dependency,
    val conditions: List<Condition>,
    val actions: List<Action>
)