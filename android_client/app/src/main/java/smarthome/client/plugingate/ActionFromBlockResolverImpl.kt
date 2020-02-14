package smarthome.client.plugingate

import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.action.Action

class ActionFromBlockResolverImpl(
    private val resolvers: List<ActionFromBlockResolver>
) : ActionFromBlockResolver {
    override fun resolve(dependencyId: DependencyId, block: Block): Action? {
        return resolvers.find { it.canResolve(block) }?.resolve(dependencyId, block)
    }
    
    override fun canResolve(block: Block) = false
}