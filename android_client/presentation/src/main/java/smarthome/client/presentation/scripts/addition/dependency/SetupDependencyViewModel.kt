package smarthome.client.presentation.scripts.addition.dependency

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import org.koin.core.qualifier.named
import smarthome.client.domain.api.scripts.usecases.GetBlockNameUseCase
import smarthome.client.domain.api.scripts.usecases.RemoveDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.*
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.ACTION_CONTAINER_VIEWMODEL
import smarthome.client.presentation.CONDITION_CONTAINER_VIEWMODEL
import smarthome.client.presentation.scripts.addition.SetupScriptViewModel
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerId
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerState
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.presentation.util.extensions.updateWith
import smarthome.client.util.replaceAt
import smarthome.client.util.truncate

class SetupDependencyViewModel: KoinViewModel() {
    private val scriptId: Long = 1L // todo
    private lateinit var dependencyId: DependencyId
    private lateinit var setupScriptViewModel: SetupScriptViewModel
    private val removeDependency: RemoveDependencyUseCase by inject()
    
    private val observeSetupDependencyUseCase: ObserveSetupDependencyUseCase by inject()
    private val startSetupDependencyUseCase: StartSetupDependencyUseCase by inject()
    private val getSetupDependencyUseCase: GetSetupDependencyUseCase by inject()
    private val getBlockNameUseCase: GetBlockNameUseCase by inject()
    private val addConditionToSetupDependencyUseCase: AddConditionToSetupDependencyUseCase by inject()
    private val removeConditionsFromSetupDependencyUseCase: RemoveConditionsFromSetupDependencyUseCase by inject()
    private val updateSetupDependencyUseCase: UpdateSetupDependencyUseCase by inject()
    
    val close = NavigationLiveData()
    
    private val conditionsViewModel: ContainersViewModel<Condition> by inject(named(CONDITION_CONTAINER_VIEWMODEL))
    private val actionsViewModel: ContainersViewModel<Action> by inject(named(ACTION_CONTAINER_VIEWMODEL))
    
    val conditionContainers = conditionsViewModel.containersLiveData
    val actionContainers = actionsViewModel.containersLiveData
    val toolbarTitle = MutableLiveData<String>()
    val selectionMode = MutableLiveData(DEFAULT_SELECTION_MODE)
    
    var isNew: Boolean = false
        private set
    
    fun onSave(conditions: List<Condition>, action: Action?) {
    
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
        startSetupDependencyUseCase.execute(scriptId, dependencyId)
        
        disposable.add(
            observeSetupDependencyUseCase.execute().subscribe(this::onSetupDependencyUpdated)
        )
    }
    
    private fun updateSetupToolbarTitle() {
        val dependency = getSetupDependencyUseCase.execute()
        val fromName = getBlockNameUseCase.execute(scriptId, dependency.startBlock)
        val toName = getBlockNameUseCase.execute(scriptId, dependency.endBlock)
        
        toolbarTitle.value = fromName.truncate(10) + " -> " + toName.truncate(10)
    }
    
    private fun onSetupDependencyUpdated(dependency: Dependency) {
        conditionsViewModel.setData(dependency.conditions, dependency)
        actionsViewModel.setData(dependency.actions, dependency)
    }
    
    fun setFlowViewModel(setupScriptViewModel: SetupScriptViewModel) {
        this.setupScriptViewModel = setupScriptViewModel
    }
    
    fun addConditionsContainer() {
        addConditionToSetupDependencyUseCase.execute()
    }
    
    fun onSelectionModeClick() {
        setSelection(selectionMode.value?.not() ?: DEFAULT_SELECTION_MODE)
    }
    
    private fun setSelection(mode: Boolean) {
        selectionMode.updateWith { mode }
        
        conditionsViewModel.setSelectionMode(mode)
        
        when (mode) {
            false -> updateSetupToolbarTitle()
            true -> updateSelectionToolbarTitle()
        }
    }
    
    fun onSelect(id: ContainerId, isSelected: Boolean) {
        conditionsViewModel.onSelect(id, isSelected)
        
        updateSelectionToolbarTitle()
        if (getSelectedConditionsCount() == 0) cancelSelection()
    }
    
    private fun updateSelectionToolbarTitle() {
        val selectedCount = getSelectedConditionsCount()
        val allCount = conditionContainers.value.orEmpty().size
        
        toolbarTitle.value = "Selected: $selectedCount of $allCount."
    }
    
    private fun getSelectedConditionsCount(): Int = conditionContainers.value.orEmpty().count { it.isSelected }
    
    fun cancelSelection() {
        setSelection(false)
    }
    
    fun onDeleteSelected() {
        val selectedConditions = conditionContainers.value.orEmpty()
            .filter { it.isSelected }
            .map { it.currentData.id }
            .toTypedArray()
    
        removeConditionsFromSetupDependencyUseCase.execute(*selectedConditions)
        cancelSelection()
    }
    
    fun onConditionScrolled(containerId: ContainerId, condition: Condition) {
        val position = indexOfContainer(conditionContainers.value, containerId)
        val setupDependency = getSetupDependencyUseCase.execute()
        val nowSelected = conditionsViewModel.getUnit(containerId, condition.id) ?: return
        val updatedConditions = setupDependency.conditions.replaceAt(position, nowSelected)
        updateSetupDependencyUseCase.execute(setupDependency.copy(conditions = updatedConditions))
    }
    
    fun onActionScrolled(containerId: ContainerId, action: Action) {
        val position = indexOfContainer(actionContainers.value, containerId)
        val setupDependency = getSetupDependencyUseCase.execute()
        val nowSelected = actionsViewModel.getUnit(containerId, action.id) ?: return
        val updatedActions = setupDependency.actions.replaceAt(position, nowSelected)
        updateSetupDependencyUseCase.execute(setupDependency.copy(actions = updatedActions))
    }
    
    private fun indexOfContainer(containers: List<ContainerState<*>>?,
                                 containerId: ContainerId): Int {
        return containers.orEmpty().indexOfFirst { it.id == containerId }
    }
    
    companion object {
        private val DEFAULT_SELECTION_MODE = false
    }
}