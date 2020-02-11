package smarthome.client.presentation.scripts.addition.dependency

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import smarthome.client.domain.api.scripts.usecases.*
import smarthome.client.entity.script.block.BlockId
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.SetupScriptViewModel
import smarthome.client.presentation.scripts.addition.dependency.action.ActionViewState
import smarthome.client.presentation.scripts.addition.dependency.condition.ConditionContainerState
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData

class SetupDependencyViewModel: KoinViewModel() {
    private val scriptId: Long = 1L // todo
    private lateinit var dependencyId: DependencyId
    private lateinit var setupScriptViewModel: SetupScriptViewModel
    private val removeDependency: RemoveDependencyUseCase by inject()
    private val getDependencyUseCase: GetDependencyUseCase by inject()
    private val createEmptyConditions: CreateEmptyConditionsForBlockUseCase by inject()
    private val createEmptyAction: CreateEmptyActionForBlockUseCase by inject()
    private val updateDependencyDetailsUseCase: UpdateDependencyDetailsUseCase by inject()
    
    private lateinit var emptyConditionsForDependency: List<Condition>
    private lateinit var emptyActionForDependency: Action
    
    val close = NavigationLiveData()
    val conditions = MutableLiveData<List<ConditionContainerState>>()
    val action = MutableLiveData<ActionViewState>()
    
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
        val dependency = getDependencyUseCase.execute(scriptId, dependencyId)
        
        initializeEmptyConditions(dependency.startBlock)
        conditions.value = listOf(
            ConditionContainerState(emptyConditionsForDependency),
            ConditionContainerState(emptyConditionsForDependency),
            ConditionContainerState(emptyConditionsForDependency),
            ConditionContainerState(emptyConditionsForDependency),
            ConditionContainerState(emptyConditionsForDependency),
            ConditionContainerState(emptyConditionsForDependency),
            ConditionContainerState(emptyConditionsForDependency),
            ConditionContainerState(emptyConditionsForDependency)
        )
    
        initializeEmptyAction(dependency.endBlock)
        action.value = ActionViewState(emptyActionForDependency)
    }
    
    private fun initializeEmptyConditions(startBlock: BlockId) {
        val conditions = createEmptyConditions.execute(scriptId, dependencyId, startBlock)
    
        emptyConditionsForDependency = conditions
    }
    
    private fun initializeEmptyAction(endBlock: BlockId) {
        val action = createEmptyAction.execute(scriptId, dependencyId, endBlock)
    
        emptyActionForDependency = action
    }
    
    fun setFlowViewModel(setupScriptViewModel: SetupScriptViewModel) {
        this.setupScriptViewModel = setupScriptViewModel
    }
    
    companion object {
        const val minimumConditions = 1
    }
}