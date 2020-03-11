package smarthome.client.presentation.scripts.addition.dependency.container.data

import smarthome.client.entity.script.dependency.condition.Condition

data class ConditionContainerData(override val units: List<Condition>) : ContainerData<Condition> {
    override fun copyWithUnits(units: List<Condition>): ContainerData<Condition> {
        return copy(units = units)
    }
}
