package smarthome.client.domain.api.scripts.resolver

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.condition.Condition

interface ConditionFromBlockResolver {
    fun resolve(dependencyId: DependencyId, block: Block): List<Condition>
    fun canResolve(block: Block): Boolean
}