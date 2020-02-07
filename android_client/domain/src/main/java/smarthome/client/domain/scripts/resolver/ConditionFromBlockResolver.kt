package smarthome.client.domain.scripts.resolver

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.controller.ControllerBlock
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.entity.script.dependency.condition.controller.ControllerValueCondition

class ConditionFromBlockResolver {
    fun resolve(dependencyId: DependencyId, block: Block): List<Condition> {
        return when (block) {
            is ControllerBlock -> listOf(ControllerValueCondition(block.controllerId, dependencyId))
            else -> emptyList()
        }
    }
}