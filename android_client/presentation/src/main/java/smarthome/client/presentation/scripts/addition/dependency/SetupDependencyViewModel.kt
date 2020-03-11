package smarthome.client.presentation.scripts.addition.dependency

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import org.koin.core.qualifier.named
import smarthome.client.domain.api.scripts.usecases.GetBlockNameUseCase
import smarthome.client.domain.api.scripts.usecases.RemoveDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.UpdateDependencyDetailsUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.AddConditionToSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.GetSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.ObserveSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.StartSetupDependencyUseCase
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.ACTION_CONTAINER_VIEWMODEL
import smarthome.client.presentation.CONDITION_CONTAINER_VIEWMODEL
import smarthome.client.presentation.scripts.addition.SetupScriptViewModel
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerId
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.presentation.util.extensions.updateWith
import smarthome.client.util.log
import smarthome.client.util.truncate

class SetupDependencyViewModel: KoinViewModel() {
    private val scriptId: Long = 1L // todo
    private lateinit var dependencyId: DependencyId
    private lateinit var setupScriptViewModel: SetupScriptViewModel
    private val removeDependency: RemoveDependencyUseCase by inject()
    
    private val updateDependencyDetailsUseCase: UpdateDependencyDetailsUseCase by inject()
    private val observeSetupDependencyUseCase: ObserveSetupDependencyUseCase by inject()
    private val startSetupDependencyUseCase: StartSetupDependencyUseCase by inject()
    private val getSetupDependencyUseCase: GetSetupDependencyUseCase by inject()
    private val getBlockNameUseCase: GetBlockNameUseCase by inject()
    private val addConditionToSetupDependencyUseCase: AddConditionToSetupDependencyUseCase by inject()
    
    val close = NavigationLiveData()
    
    private val conditionsViewModel: ContainersViewModel<Condition> by inject(named(CONDITION_CONTAINER_VIEWMODEL))
    private val actionsViewModel: ContainersViewModel<Action> by inject(named(ACTION_CONTAINER_VIEWMODEL))
    
    val conditionContainers = conditionsViewModel.containers
    val actionContainers = actionsViewModel.containers
    val toolbarTitle = MutableLiveData<String>()
    val selectionMode = MutableLiveData(DEFAULT_SELECTION_MODE)
    
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
            observeSetupDependencyUseCase.execute().subscribe(this::synchronizeContainers)
        )
        startSetupDependencyUseCase.execute(scriptId, dependencyId)
    }
    
    private fun updateSetupToolbarTitle() {
        val details = getSetupDependencyUseCase.execute()
        val fromName = getBlockNameUseCase.execute(scriptId, details.dependency.startBlock)
        val toName = getBlockNameUseCase.execute(scriptId, details.dependency.endBlock)
        
        toolbarTitle.value = fromName.truncate(10) + " -> " + toName.truncate(10)
    }
    
    private fun synchronizeContainers(details: DependencyDetails) {
        conditionsViewModel.setData(details.conditions, details)
        actionsViewModel.setData(details.actions, details)
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
        
        conditionContainers.updateWith { conditions ->
            conditions.orEmpty().map { condition ->
                condition.copy(
                    isSelected = if (mode) condition.isSelected else false,
                    selectionMode = mode
                )
            }
        }
        
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
        log("delete")
    }
    
    companion object {
        private val DEFAULT_SELECTION_MODE = false
    }
}