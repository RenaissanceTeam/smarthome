package smarthome.client.presentation.scripts.resolver

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.domain.api.scripts.resolver.Resolver
import smarthome.client.entity.script.dependency.condition.Condition

interface ConditionModelResolver: Resolver<Condition, EpoxyModel<*>>