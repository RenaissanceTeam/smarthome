package smarthome.client.presentation.scripts.addition.dependency.container.condition

import smarthome.client.entity.script.dependency.DependencyUnit
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerData

data class ConditionContainerData(override val units: List<Condition>) : ContainerData {
    override fun copyWithUnits(units: List<DependencyUnit>): ContainerData {
        return copy(units = units.map { it as Condition })
    }
}
