package smarthome.client.plugingate

import android.content.Context
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.presentation.scripts.addition.dependency.action.ActionView
import smarthome.client.presentation.scripts.resolver.ActionViewResolver

class ActionViewResolverImpl(
    private val resolvers: List<ActionViewResolver>
): ActionViewResolver {
    override fun canResolve(action: Action) = false
    
    override fun resolve(context: Context, action: Action): ActionView? {
        return resolvers.find { it.canResolve(action) }?.resolve(context, action)
    }
}