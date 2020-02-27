package smarthome.client.presentation.scripts.addition.dependency

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import smarthome.client.domain.api.scripts.usecases.*
import smarthome.client.domain.api.scripts.usecases.dependency.GetSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.ObserveSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.StartSetupDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.DependencyUnit
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.scripts.addition.SetupScriptViewModel
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerData
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerId
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerState
import smarthome.client.presentation.scripts.addition.dependency.container.action.ActionContainerData
import smarthome.client.presentation.scripts.addition.dependency.container.condition.ConditionContainerData
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.util.*

class SetupDependencyViewModel: KoinViewModel() {
    private val scriptId: Long = 1L // todo
    private lateinit var dependencyId: DependencyId
    private lateinit var setupScriptViewModel: SetupScriptViewModel
    private val removeDependency: RemoveDependencyUseCase by inject()
    
    private val createEmptyConditions: CreateEmptyConditionsForBlockUseCase by inject()
    private val createEmptyActions: CreateEmptyActionForBlockUseCase by inject()
    private val updateDependencyDetailsUseCase: UpdateDependencyDetailsUseCase by inject()
    private val observeSetupDependencyUseCase: ObserveSetupDependencyUseCase by inject()
    private val startSetupDependencyUseCase: StartSetupDependencyUseCase by inject()
    private val getSetupDependencyUseCase: GetSetupDependencyUseCase by inject()
    private val toolbarController: ToolbarController by inject()
    private val getBlockNameUseCase: GetBlockNameUseCase by inject()
    
    val close = NavigationLiveData()
    val conditionContainers = MutableLiveData<List<ContainerState>>()
    val actionContainers = MutableLiveData<List<ContainerState>>()
    
    var isNew: Boolean = false
        private set
    
    fun onSave(conditions: List<Condition>, action: Action?) {
        if (conditions.isEmpty() || action == null) return
        
        updateDependencyDetailsUseCase
            .runCatching { execute(scriptId, dependencyId, conditions, action) }
            .onSuccess { close.trigger() }
    }
    
    fun onCancel() {
        removeDependency.execute(setupScriptViewModel.scriptId, dependencyId)
        close.trigger()
    }
    
    fun setIsNew(isNew: Boolean) {
        this.isNew = isNew
    }
    
    fun setDependencyId(id: DependencyId) {
        dependencyId = id
        
        disposable.add(
            observeSetupDependencyUseCase.execute()
                .subscribe(this::synchronizeContainers)
        )
        val dependency = startSetupDependencyUseCase.execute(scriptId, dependencyId)
        initializeContainers(dependency)
    
        toolbarController.setTitle(getTitleForToolbar())
    }
    
    private fun getTitleForToolbar(): String {
        val details = getSetupDependencyUseCase.execute()
        val fromName = getBlockNameUseCase.execute(scriptId, details.dependency.startBlock)
        val toName = getBlockNameUseCase.execute(scriptId, details.dependency.endBlock)
        
        return fromName.truncate(10) + " -> " + toName.truncate(10)
    }
    
    private fun initializeContainers(dependency: DependencyDetails) {
        actionContainers.value = bindUnitsToContainers(
            dependency.actions,
            actionContainers.value.orEmpty()
        )
        
        conditionContainers.value = bindUnitsToContainers(
            dependency.conditions,
            conditionContainers.value.orEmpty()
        )
    }
    
    private fun synchronizeContainers(details: DependencyDetails) {
        conditionContainers.value = createContainersIfNoneExisted(
            conditionContainers.value.orEmpty(),
            details.conditions,
            details
        )
        
        actionContainers.value = createContainersIfNoneExisted(
            actionContainers.value.orEmpty(),
            details.actions,
            details
        )
    }
    
    private fun bindUnitsToContainers(units: List<DependencyUnit>,
                                      containers: List<ContainerState>): List<ContainerState> {
        return units.mapIndexed { index, dependencyUnit ->
            val container = containers[index]
            val newData = replaceUnitInContainerData(dependencyUnit, container.data)
            
            val selectedIndex = newData.units.indexOf(dependencyUnit)
            container.copy(data = newData, selected = selectedIndex)
        }
    }
    
    private fun replaceUnitInContainerData(unit: DependencyUnit, data: ContainerData): ContainerData {
        val currentUnits = data.units
        val replacedUnits = currentUnits.findAndModify(
            predicate = { stored: DependencyUnit -> stored.id == unit.id && stored.data != unit.data },
            modify = { unit }
        )
        return data.copyWithUnits(replacedUnits)
    }
    
    private fun createContainersIfNoneExisted(containers: List<ContainerState>,
                                              units: List<DependencyUnit>,
                                              details: DependencyDetails): List<ContainerState> {
        var allContainers = containers
        units.forEachIndexed { i, unit ->
            if (checkIfContainerHasUnit(i, containers, unit)) return@forEachIndexed
            allContainers = allContainers.withInserted(i, createNewContainerForUnit(unit, details.dependency,
                containerDataFactory = this::createContainerData,
                replaceEmptyPredicate = { it.data::class == unit.data::class }
            ))
        }
        return allContainers
    }
    
    private fun createContainerData(units: List<DependencyUnit>): ContainerData {
        return when (units.firstOrNull()) {
            is Action -> ActionContainerData(units.map { it as Action })
            is Condition -> ConditionContainerData(units.map { it as Condition })
            else -> throw IllegalStateException(
                "Can't create container data with units ${units.joinToString(", ")}"
            )
        }
    }
    
    private fun checkIfContainerHasUnit(index: Int,
                                        containers: List<ContainerState>,
                                        unit: DependencyUnit): Boolean {
        val containerData = containers.runCatching { get(index).data }.getOrElse { return false }
        return containerData.units.containsThat { it.id == unit.id }
    }
    
    private fun createNewContainerForUnit(unit: DependencyUnit, dependency: Dependency,
                                          replaceEmptyPredicate: (DependencyUnit) -> Boolean,
                                          containerDataFactory: (List<DependencyUnit>) -> ContainerData): ContainerState {
        val emptyUnits = createEmptyUnitsForContainer(unit, dependency)
        
        val filledUnits = emptyUnits.findAndModify(
            predicate = replaceEmptyPredicate,
            modify = { unit }
        )
        val selectedIndex = filledUnits.indexOf(unit)
    
    
        return ContainerState(ContainerId(), containerDataFactory(filledUnits), selectedIndex)
    }
    
    private fun createEmptyUnitsForContainer(unit: DependencyUnit,
                                             dependency: Dependency): List<DependencyUnit> {
        return when (unit) {
            is Action -> createEmptyActions.execute(scriptId, dependency.endBlock)
            is Condition -> createEmptyConditions.execute(scriptId, dependency.startBlock)
            else -> throw IllegalArgumentException("Cannot create empty units for $unit")
        }
    }
    
    fun setFlowViewModel(setupScriptViewModel: SetupScriptViewModel) {
        this.setupScriptViewModel = setupScriptViewModel
    }
}