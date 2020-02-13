package smarthome.client.presentation.scripts.addition.dependency.container.action

import smarthome.client.entity.script.dependency.DependencyUnit
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerData

data class ActionContainerData(override val units: List<Action>) : ContainerData {
    override fun copyWithUnits(units: List<DependencyUnit>): ContainerData {
        return copy(units = units.map { it as Action })
    }
}