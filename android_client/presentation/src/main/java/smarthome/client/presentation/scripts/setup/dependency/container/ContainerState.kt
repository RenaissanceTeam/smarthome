package smarthome.client.presentation.scripts.setup.dependency.container

import smarthome.client.entity.script.dependency.DependencyUnit

data class ContainerState<out T : DependencyUnit>(
    val id: ContainerId,
    val allData: List<T>,
    val currentData: T,
    val selectionMode: Boolean = false,
    val isSelected: Boolean = false
)