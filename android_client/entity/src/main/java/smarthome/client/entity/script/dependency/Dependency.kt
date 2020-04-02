package smarthome.client.entity.script.dependency

import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition

data class Dependency(
    val id: String,
    val startBlock: String,
    val endBlock: String,
    val conditions: List<Condition> = emptyList(),
    val actions: List<Action> = emptyList()
)