package smarthome.client.presentation.scripts.addition.dependency.container.condition

import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerId
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerData

data class ConditionContainerData(val conditions: List<Condition>) : ContainerData
