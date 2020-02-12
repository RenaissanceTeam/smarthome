package smarthome.client.presentation.scripts.addition.dependency

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import smarthome.client.domain.api.scripts.usecases.*
import smarthome.client.domain.api.scripts.usecases.dependency.GetSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.StartSetupDependencyUseCase
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.SetupScriptViewModel
import smarthome.client.presentation.scripts.addition.dependency.action.ActionViewState
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerId
import smarthome.client.presentation.scripts.addition.dependency.container.condition.ConditionContainerState
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.util.findAndModify

class SetupDependencyViewModel: KoinViewModel() {
    private val scriptId: Long = 1L // todo
    private lateinit var dependencyId: DependencyId
    private lateinit var setupScriptViewModel: SetupScriptViewModel
    private val removeDependency: RemoveDependencyUseCase by inject()
    
    private val createEmptyConditions: CreateEmptyConditionsForBlockUseCase by inject()
    private val createEmptyAction: CreateEmptyActionForBlockUseCase by inject()
    private val updateDependencyDetailsUseCase: UpdateDependencyDetailsUseCase by inject()
    private val conditionModelsResolver: ConditionModelResolver by inject()
    private val getSetupDependencyUseCase: GetSetupDependencyUseCase by inject()
    private val getDependencyDetailsUseCase: GetDependencyDetailsUseCase by inject()
    private val startSetupDependencyUseCase: StartSetupDependencyUseCase by inject()
    
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
    
        val dependencyDetails = startSetupDependencyUseCase.execute(scriptId, dependencyId)
        
        val emptyConditions = createEmptyConditions.execute(scriptId,
            dependencyDetails.dependency.startBlock)
    
        val newStates = dependencyDetails.conditions.map { condition ->
            val allConditionInContainer = emptyConditions.findAndModify(
                predicate = { it.data::class == condition.data::class },
                modify = { condition }
            )
            val selectedIndex = allConditionInContainer.indexOf(condition)
    
            ConditionContainerState(ContainerId(), allConditionInContainer, selectedIndex)
        }
        conditions.value = newStates
    }
    
    fun setFlowViewModel(setupScriptViewModel: SetupScriptViewModel) {
        this.setupScriptViewModel = setupScriptViewModel
    }
}