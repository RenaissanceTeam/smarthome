package smarthome.client.presentation.scripts.addition.dependency

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import smarthome.client.domain.api.scripts.usecases.*
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.SetupScriptViewModel
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData

class SetupDependencyViewModel: KoinViewModel() {
    private val scriptId: Long = 1L // todo
    private lateinit var dependencyId: DependencyId
    private lateinit var setupScriptViewModel: SetupScriptViewModel
    private val removeDependency: RemoveDependencyUseCase by inject()
    private val observeDetails: ObserveDependencyDetailsUseCase by inject()
    private val getDependencyUseCase: GetDependencyUseCase by inject()
    private val createEmptyConditions: CreateEmptyConditionsForBlockUseCase by inject()
    private val fetchDependencyDetails: FetchDependencyDetailsUseCase by inject()
    
    private lateinit var emptyConditionsForDependency: List<Condition>
    val close = NavigationLiveData()
    val containers = MutableLiveData<List<ConditionContainerState>>()
    
    var isNew: Boolean = false
        private set
    
    fun onSave() {
    
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
        
        initializeEmptyConditions()
        initializeConditionContainers()
    }
    private fun initializeEmptyConditions() {
        val dependency = getDependencyUseCase.execute(scriptId, dependencyId)
        val conditions = createEmptyConditions.execute(scriptId, dependencyId,
            dependency.startBlock)
    
        emptyConditionsForDependency = conditions
    }
    
    private fun initializeConditionContainers() {
        containers.value = containers.value.orEmpty() + ConditionContainerState()
    }
    
    fun setFlowViewModel(setupScriptViewModel: SetupScriptViewModel) {
        this.setupScriptViewModel = setupScriptViewModel
    }
    
    fun getEmptyConditions(): List<Condition> {
        return emptyConditionsForDependency
    }
    
    companion object {
        const val minimumConditions = 1
    }
}