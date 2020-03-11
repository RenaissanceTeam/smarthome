package smarthome.client.presentation.scripts.addition.dependency.container.data

import smarthome.client.entity.script.dependency.action.Action

data class ActionContainerData(override val units: List<Action>) : ContainerData<Action> {
    
    override fun copyWithUnits(units: List<Action>): ContainerData<Action> {
        return copy(units = units)
    }
}