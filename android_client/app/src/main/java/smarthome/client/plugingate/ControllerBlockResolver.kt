package smarthome.client.plugingate

import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.entity.Controller
import smarthome.client.entity.script.block.Block
import smarthome.client.util.Position

class ControllerBlockResolverImpl(
    private val resolvers: List<ControllerBlockResolver>
) : ControllerBlockResolver {
    
    override fun canResolve(controller: Controller) = false
    
    override fun resolve(controller: Controller, position: Position): Block? {
        return resolvers.find { it.canResolve(controller) }?.resolve(controller, position)
    }
}