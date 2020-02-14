package smarthome.client.plugingate

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.domain.api.scripts.resolver.Resolver
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

class ConditionModelResolverImpl(private val resolvers: List<ConditionModelResolver>) :
    Resolver<Condition, EpoxyModel<*>> by ResolverFinder(resolvers), ConditionModelResolver