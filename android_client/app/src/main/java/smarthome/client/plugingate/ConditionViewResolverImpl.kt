package smarthome.client.plugingate

import android.content.Context
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.dependency.ConditionView
import smarthome.client.presentation.scripts.resolver.ConditionViewResolver

class ConditionViewResolverImpl(private val resolvers: List<ConditionViewResolver>) : ConditionViewResolver {
    override fun canResolve(condition: Condition) = false
    
    override fun resolve(context: Context, condition: Condition): ConditionView? {
        return resolvers.find { it.canResolve(condition) }?.resolve(context, condition)
    }
}