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
import smarthome.client.presentation.scripts.addition.SetupScriptViewModel
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerId
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerState
import smarthome.client.presentation.scripts.addition.dependency.container.action.ActionContainerData
import smarthome.client.presentation.scripts.addition.dependency.container.action.ActionContainerState
import smarthome.client.presentation.scripts.addition.dependency.container.condition.ConditionContainerData
import smarthome.client.presentation.scripts.addition.dependency.container.condition.ConditionContainerState
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.util.containsThat
import smarthome.client.util.findAndModify
import smarthome.client.util.withInserted

class SetupDependencyViewModel: KoinViewModel() {
    private val scriptId: Long = 1L // todo
    private lateinit var dependencyId: DependencyId
    private lateinit var setupScriptViewModel: SetupScriptViewModel
    private val removeDependency: RemoveDependencyUseCase by inject()
    
    private val createEmptyConditions: CreateEmptyConditionsForBlockUseCase by inject()
    private val createEmptyActions: CreateEmptyActionForBlockUseCase by inject()
    private val updateDependencyDetailsUseCase: UpdateDependencyDetailsUseCase by inject()
    private val conditionModelsResolver: ConditionModelResolver by inject()
    private val getSetupDependencyUseCase: GetSetupDependencyUseCase by inject()
    private val observeSetupDependencyUseCase: ObserveSetupDependencyUseCase by inject()
    private val getDependencyDetailsUseCase: GetDependencyDetailsUseCase by inject()
    private val startSetupDependencyUseCase: StartSetupDependencyUseCase by inject()
    
    val close = NavigationLiveData()
    val conditionContainers = MutableLiveData<List<ContainerState>>()
    val actionContainers = MutableLiveData<List<ActionContainerState>>()
    
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
    
        val dependencyDetails = startSetupDependencyUseCase.execute(scriptId, dependencyId)
        
        initConditions(dependencyDetails)
        initActions(dependencyDetails)
    
        disposable.add(
            observeSetupDependencyUseCase.execute().subscribe(this::bindDependencyDetails)
        )
    }

    private fun bindDependencyDetails(details: DependencyDetails) {
        conditionContainers.value = createContainersIfNoneExisted(
            conditionContainers.value.orEmpty(),
            details.conditions,
            details
        )
    
        conditionContainers.value = details.conditions
            .map { condition ->
                val containerWithCondition = conditionContainers.value.orEmpty()
                    .find { container ->
                        container.conditions.map { it.id }.contains(condition.id)
                    } ?: throw Throwable("No container with condition, but it should've been added")
            
                val modified = containerWithCondition.conditions.findAndModify(
                    predicate = { it.data::class == condition.data::class },
                    modify = { condition }
                )
                val selectedIndex = modified.indexOf(condition)
            
                containerWithCondition.copy(conditions = modified, selected = selectedIndex)
            }
    
        actionContainers.value = details.actions
            .also { }
    }
    
    private fun createContainersIfNoneExisted(containers: List<ContainerState>,
                                              units: List<DependencyUnit>,
                                              details: DependencyDetails): List<ContainerState> {
        var allContainers = containers
        units.forEachIndexed { i, unit ->
            if (checkIfContainerHasDependencyData(i, containers, details)) return@forEachIndexed
            allContainers = allContainers.withInserted(i, createNewContainerForUnit(unit, details.dependency, getReplaceEmptyPredicateForUnit(unit)))
        }
        return allContainers
    }
    
    private fun getReplaceEmptyPredicateForUnit(unit: DependencyUnit): (DependencyUnit) -> Boolean {
        return when (unit) {
            is Action -> { empty: Action -> empty.data::class == unit.data::class }
            is Condition -> { empty: Condition -> empty.data::class == unit.data::class }
            else -> { _ -> false }
        }
    }
    
    private fun checkIfContainerHasDependencyData(index: Int,
                                                  containers: List<ContainerState>,
                                                  details: DependencyDetails): Boolean {
        val containerData = containers.runCatching { get(index).data }.getOrElse { return false }
        return when (containerData) {
            is ConditionContainerData -> {
                containerData.conditions.containsThat { it.id == details.conditions[index].id }
            }
            is ActionContainerData -> {
                containerData.actions.containsThat { it.id == details.actions[index].id }
            }
            else -> throw IllegalArgumentException("Unknown type of container data $containerData")
        }
    }
    
    private fun addNewActionContainersIfNeeded(actions: List<Action>, details: DependencyDetails) {
        var containers = conditionContainers.value.orEmpty()
        actions.forEachIndexed { index, action ->
            containers.runCatching {
                get(index).conditions.containsThat { it.id == action.id }
            }.onFailure {
                containers = containers.withInserted(index, createNewContainer(details.dependency, action))
            }
        }
        conditionContainers.value = containers
    }
    
    private fun createNewContainerForUnit(unit: DependencyUnit, dependency: Dependency,
                                          replaceEmptyPredicate: (DependencyUnit) -> Boolean): ContainerState {
        val emptyUnits = createEmptyUnitsForContainer(unit, dependency)
        
        val filledUnits = emptyUnits.findAndModify(
            predicate = { emptyUnit -> emptyUnit.data::class == condition.data::class },
            modify = { unit }
        )
        val selectedIndex = filledUnits.indexOf(unit)
        
        
        return ContainerState(ContainerId(), ConditionContainerData(filledUnits), selectedIndex)
    }
    
    private fun createEmptyUnitsForContainer(unit: DependencyUnit,
                                             dependency: Dependency): List<DependencyUnit> {
        return when (unit) {
            is Action -> createEmptyActions.execute(scriptId, dependency.endBlock)
            is Condition -> createEmptyConditions.execute(scriptId, dependency.startBlock)
            else -> throw IllegalArgumentException("Cannot create empty units for $unit")
        }
    }
    
    fun createNewActionsContainer(dependency: Dependency, action: Action) {
        val emptyActions = createEmptyActions.execute(scriptId, dependency.endBlock)
        
        val containerUnits = emptyActions.findAndModify(
            predicate = { it.data::class == action.data::class },
            modify = { action }
        )
        val selectedIndex = containerUnits.indexOf(action)
    }
    
    private fun initConditions(dependencyDetails: DependencyDetails) {
        val emptyConditions = createEmptyConditions.execute(scriptId,
            dependencyDetails.dependency.startBlock)
    
        conditionContainers.value = dependencyDetails.conditions.map { condition ->
            val containerUnits = emptyConditions.findAndModify(
                predicate = { it.data::class == condition.data::class },
                modify = { condition }
            )
            val selectedIndex = containerUnits.indexOf(condition)
            
            ConditionContainerState(ContainerId(), containerUnits, selectedIndex)
        }
    }
    
    // todo add tests then refactor out copy paste
    private fun initActions(dependencyDetails: DependencyDetails) {
        val emptyActions = createEmptyActions.execute(scriptId,
            dependencyDetails.dependency.endBlock)
    
        actionContainers.value = dependencyDetails.actions.map { action ->
            val allActionsInContainer = emptyActions.findAndModify(
                predicate = { it.data::class == action.data::class },
                modify = { action }
            )
            val selectedIndex = allActionsInContainer.indexOf(action)
            
            ActionContainerState(ContainerId(), allActionsInContainer, selectedIndex)
        }
    }
    
    fun setFlowViewModel(setupScriptViewModel: SetupScriptViewModel) {
        this.setupScriptViewModel = setupScriptViewModel
    }
}