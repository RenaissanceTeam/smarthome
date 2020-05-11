package smarthome.client.presentation.scripts.setup.dependency

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import org.koin.core.qualifier.named
import smarthome.client.domain.api.scripts.usecases.dependency.*
import smarthome.client.domain.api.scripts.usecases.setup.GetBlockNameUseCase
import smarthome.client.domain.api.scripts.usecases.setup.RemoveDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.ACTION_CONTAINER_VIEWMODEL
import smarthome.client.presentation.CONDITION_CONTAINER_VIEWMODEL
import smarthome.client.presentation.scripts.setup.SetupScriptViewModel
import smarthome.client.presentation.scripts.setup.dependency.container.ContainerId
import smarthome.client.presentation.scripts.setup.dependency.container.ContainerState
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.presentation.util.extensions.updateWith
import smarthome.client.util.log
import smarthome.client.util.replaceAt
import smarthome.client.util.truncate

class SetupDependencyViewModel : KoinViewModel() {
    private lateinit var dependencyId: String
    private lateinit var setupScriptViewModel: SetupScriptViewModel
    private val removeDependency: RemoveDependencyUseCase by inject()
    private val observeSetupDependencyUseCase: ObserveSetupDependencyUseCase by inject()
    private val startSetupDependencyUseCase: StartSetupDependencyUseCase by inject()
    private val getSetupDependencyUseCase: GetSetupDependencyUseCase by inject()
    private val getBlockNameUseCase: GetBlockNameUseCase by inject()
    private val addConditionToSetupDependencyUseCase: AddConditionToSetupDependencyUseCase by inject()
    private val removeConditionsFromSetupDependencyUseCase: RemoveConditionsFromSetupDependencyUseCase by inject()
    private val updateSetupDependencyUseCase: UpdateSetupDependencyUseCase by inject()
    private val saveSetupDependencyUseCase: SaveSetupDependencyUseCase by inject()
    private val conditionsViewModel: ContainersViewModel<Condition> by inject(named(CONDITION_CONTAINER_VIEWMODEL))
    private val actionsViewModel: ContainersViewModel<Action> by inject(named(ACTION_CONTAINER_VIEWMODEL))
    
    val close = NavigationLiveData()
    val conditionContainers = conditionsViewModel.containersLiveData
    val actionContainers = actionsViewModel.containersLiveData
    val toolbarTitle = MutableLiveData<String>()
    val selectionMode = MutableLiveData(DEFAULT_SELECTION_MODE)
    
    var isNew: Boolean = false
        private set
    
    fun onSave() {
        saveSetupDependencyUseCase.execute()
        close.trigger()
    }
    
    fun onCancel() {
        if (isNew) removeDependency.execute(dependencyId)
        close.trigger()
    }
    
    fun setIsNew(isNew: Boolean) {
        this.isNew = isNew
    }
    
    fun setDependencyId(id: String) {
        dependencyId = id
        disposable.add(
                observeSetupDependencyUseCase.execute().subscribe(this::onSetupDependencyUpdated)
        )
        startSetupDependencyUseCase.execute(dependencyId)
        updateSetupToolbarTitle()
    }
    
    private fun updateSetupToolbarTitle() {
        val dependency = getSetupDependencyUseCase.execute()
        val fromName = getBlockNameUseCase.execute(dependency.startBlock)
        val toName = getBlockNameUseCase.execute(dependency.endBlock)
        
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

    fun onDeleteDependency() {
        removeDependency.execute(dependencyId)
        close.trigger()
    }

    companion object {
        private val DEFAULT_SELECTION_MODE = false
    }
}