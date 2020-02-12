package smarthome.client.presentation.scripts.addition.dependency.container.condition

import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.entity.script.dependency.condition.DependencyUnitId


data class ContainerState(val id: DependencyUnitId,
                          val conditions: List<Condition>,
                          val selected: Int
)