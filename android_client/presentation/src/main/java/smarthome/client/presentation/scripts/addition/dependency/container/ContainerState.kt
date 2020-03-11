package smarthome.client.presentation.scripts.addition.dependency.container

import smarthome.client.entity.script.dependency.DependencyUnit

data class ContainerState<out T : DependencyUnit>(
    val id: ContainerId,
    val data: List<T>,
    val selectedUnitIndex: Int,
    val selectionMode: Boolean = false,
    val isSelected: Boolean = false
)