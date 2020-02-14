package smarthome.client.plugingate

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.domain.api.scripts.resolver.Resolver
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.presentation.scripts.resolver.ActionModelResolver

class ActionModelResolverImpl(private val resolvers: List<ActionModelResolver>) :
    Resolver<Action, EpoxyModel<*>> by ResolverFinder(resolvers), ActionModelResolver