package smarthome.client.presentation.scripts.addition.dependency.container

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.domain.api.scripts.resolver.Resolver
import smarthome.client.entity.script.dependency.DependencyUnit

interface DependencyUnitModelResolver<T: DependencyUnit>: Resolver<T, EpoxyModel<*>>