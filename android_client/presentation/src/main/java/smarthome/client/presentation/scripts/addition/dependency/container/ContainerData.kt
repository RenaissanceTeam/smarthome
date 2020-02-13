package smarthome.client.presentation.scripts.addition.dependency.container

import smarthome.client.entity.script.dependency.DependencyUnit

interface ContainerData {
    val units: List<DependencyUnit>
    
    fun copyWithUnits(units: List<DependencyUnit> = this.units): ContainerData
}