package smarthome.client.presentation.scripts.addition.dependency

import androidx.lifecycle.MutableLiveData
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.domain.api.scripts.usecases.CreateEmptyActionForBlockUseCase
import smarthome.client.domain.api.scripts.usecases.CreateEmptyConditionsForBlockUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyUnit
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerId
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerState
import smarthome.client.presentation.util.extensions.updateWith
import smarthome.client.util.containsAny
import smarthome.client.util.containsThat
import smarthome.client.util.findAndModify

class ContainersViewModel<T : DependencyUnit> : KoinComponent {
    val scriptId = 1L // TODO
    private val createEmptyConditions: CreateEmptyConditionsForBlockUseCase by inject()
    private val createEmptyActions: CreateEmptyActionForBlockUseCase by inject()
    val containers = MutableLiveData<List<ContainerState<T>>>()
    
    fun setData(data: List<T>, details: DependencyDetails) {
        val currentContainers = containers.value.orEmpty()
        val withAddedContainers = createContainersIfNoneExisted(currentContainers, data, details)
        val withRemovedContainers = retainOnlyDataContainers(withAddedContainers, data)
        containers.value = withRemovedContainers
    }
    
    private fun createContainersIfNoneExisted(containers: List<ContainerState<T>>,
                                              data: List<T>,
                                              details: DependencyDetails): List<ContainerState<T>> {
        val allContainers = containers.toMutableList()
        
        data.forEachIndexed { i, unit ->
            if (checkIfContainerHasUnit(i, allContainers, unit)) return@forEachIndexed
            
            val newContainer = createNewContainerForUnit(unit, details.dependency,
                replaceEmptyPredicate = { it.data::class == unit.data::class }
            )
            allContainers.add(i, newContainer)
        }
        return allContainers
    }
    
    private fun retainOnlyDataContainers(containers: List<ContainerState<T>>,
                                         data: List<T>): List<ContainerState<T>> {
        val dataUnitIds = data.map { it.id }
        
        return containers.filter { container ->
            container.data.map { it.id }.containsAny(dataUnitIds)
        }
    }
    
    private fun checkIfContainerHasUnit(index: Int,
                                        containers: List<ContainerState<*>>,
                                        unit: DependencyUnit): Boolean {
        val containerData = containers.runCatching { get(index).data }.getOrElse { return false }
        return containerData.containsThat { it.id == unit.id }
    }
    
    private fun createNewContainerForUnit(unit: T, dependency: Dependency,
                                          replaceEmptyPredicate: (T) -> Boolean): ContainerState<T> {
        val emptyUnits = createEmptyUnitsForContainer(unit, dependency)
        
        val filledUnits = emptyUnits.findAndModify(
            predicate = replaceEmptyPredicate,
            modify = { unit }
        )
        val selectedIndex = filledUnits.indexOf(unit)
        
        return ContainerState(ContainerId(), filledUnits, selectedIndex)
    }
    
    private fun createEmptyUnitsForContainer(unit: T, dependency: Dependency): List<T> {
        return when (unit) {
            is Action -> createEmptyActions.execute(scriptId, dependency.endBlock) as List<T>
            is Condition -> createEmptyConditions.execute(scriptId, dependency.startBlock) as List<T>
            else -> throw IllegalArgumentException("Cannot create empty units for $unit")
        }
    }
    
    fun onSelect(id: ContainerId, isSelected: Boolean) {
        containers.updateWith {
            it.orEmpty().findAndModify(
                { it.id == id },
                { it.copy(isSelected = isSelected) }
            )
        }
    }
}