package smarthome.client.presentation.scripts.resolver

import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerModelResolver

interface ConditionModelResolver : ContainerModelResolver<Condition>
