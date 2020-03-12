package smarthome.client.entity.script.dependency

import smarthome.client.entity.script.block.BlockId
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition

data class Dependency(
    val id: DependencyId,
    val startBlock: BlockId,
    val endBlock: BlockId,
    val conditions: List<Condition> = emptyList(),
    val actions: List<Action> = emptyList()
)