package smarthome.client.plugingate

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.presentation.scripts.resolver.ActionModelResolver

class ActionModelResolverImpl(private val resolvers: List<ActionModelResolver>) : ActionModelResolver {
    override fun canResolve(action: Action) = false
    
    override fun resolve(action: Action): EpoxyModel<*> {
        return resolvers.find { it.canResolve(action) }?.resolve(action)
            ?: throw IllegalStateException("No resolvers found that can resolve $action")
    }
}