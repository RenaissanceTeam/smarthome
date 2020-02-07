package smarthome.client.plugingate

import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.condition.Condition

class ConditionFromBlockResolverImpl(
    private val resolvers: List<ConditionFromBlockResolver>
): ConditionFromBlockResolver {
    
    override fun resolve(dependencyId: DependencyId, block: Block): List<Condition> {
        return resolvers.find { it.canResolve(block) }?.resolve(dependencyId, block) ?: emptyList()
    }
    
    override fun canResolve(block: Block) = false
}