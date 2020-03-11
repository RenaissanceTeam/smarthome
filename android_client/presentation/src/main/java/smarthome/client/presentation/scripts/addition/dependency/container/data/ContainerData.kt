package smarthome.client.presentation.scripts.addition.dependency.container.data

import smarthome.client.entity.script.dependency.DependencyUnit

interface ContainerData<T: DependencyUnit> {
    val units: List<T>
    
    fun copyWithUnits(units: List<T> = this.units): ContainerData<T>
}