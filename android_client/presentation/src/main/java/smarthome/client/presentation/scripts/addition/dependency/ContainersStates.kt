package smarthome.client.presentation.scripts.addition.dependency

import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyUnit
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerId
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerState
import smarthome.client.util.*

class ContainersStates<T : DependencyUnit>(
    private val emptyUnitsCreator: (Dependency) -> List<T>
) {
    private val states = mutableListOf<ContainerState<T>>()
    fun asList(): List<ContainerState<T>> = states
    
    fun setData(data: List<T>, dependency: Dependency) {
        retainOnlyDataContainers(data)
        createContainersIfNoneExisted(data, dependency)
        updateData(data)
    }
    
    private fun createContainersIfNoneExisted(data: List<T>, dependency: Dependency) {
        data.forEachIndexed { i, unit ->
            if (checkIfContainerHasUnit(i, unit)) return@forEachIndexed
            
            val newContainer = createNewContainerForUnit(unit, dependency)
            states.add(i, newContainer)
        }
    }
    
    private fun retainOnlyDataContainers(data: List<T>) {
        val dataUnitIds = data.map { it.id }
        
        states.filterRemove { container ->
            container.allData.map { it.id }.containsNone(dataUnitIds)
        }
    }
    
    private fun updateData(data: List<T>) {
        val iterator = states.listIterator()
        val dataIterator = data.iterator()
        
        while (iterator.hasNext()) {
            val state = iterator.next()
            val newData = dataIterator.next()
            
            iterator.set(state.copy(
                allData = state.allData.findAndModify({ it.id == newData.id }, { newData }),
                currentData = newData
            ))
        }
    }
    
    private fun checkIfContainerHasUnit(index: Int, unit: DependencyUnit): Boolean {
        val containerData = states.runCatching { get(index).allData }.getOrElse { return false }
        return containerData.containsThat { it.id == unit.id }
    }
    
    private fun createNewContainerForUnit(unit: T, dependency: Dependency): ContainerState<T> {
        val emptyUnits = emptyUnitsCreator(dependency)
        
        val filledUnits = emptyUnits.findAndModify(
            predicate = { it.data::class == unit.data::class },
            modify = { unit }
        )
        
        return ContainerState(
            id = ContainerId(),
            allData = filledUnits,
            currentData = unit
        )
    }
}