package smarthome.client.plugingate

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

class ConditionModelResolverImpl(private val resolvers: List<ConditionModelResolver>) : ConditionModelResolver {
    override fun canResolve(condition: Condition) = false
    
    override fun resolve(condition: Condition): EpoxyModel<*> {
        return resolvers.find { it.canResolve(condition) }?.resolve(condition)
            ?: throw IllegalStateException("No resolvers found that can resolve $condition")
    }
}

