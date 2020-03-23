package smarthome.client.presentation.scripts.resolver

import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.setup.dependency.container.DependencyUnitModelResolver

interface ConditionModelResolver : DependencyUnitModelResolver<Condition>
