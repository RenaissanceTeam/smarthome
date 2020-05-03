package smarthome.client.plugingate

import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.entity.Controller
import smarthome.client.entity.script.block.ControllerBlock

class ControllerBlockResolverImpl(
    private val resolvers: List<ControllerBlockResolver>
) : ControllerBlockResolver {
    
    override fun canResolve(item: Controller) = false
    
    override fun resolve(item: Controller): ControllerBlock {
        return resolvers.find { it.canResolve(item) }?.resolve(item)
            ?: throw IllegalArgumentException("Can't resolve $item")
    }
}