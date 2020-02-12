package smarthome.client.presentation.scripts.addition.dependency.container.condition

import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerId


data class ConditionContainerState(val id: ContainerId,
                                   val conditions: List<Condition>,
                                   val selected: Int
)