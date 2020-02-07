package smarthome.client.presentation.scripts.addition.dependency

import org.koin.core.inject
import smarthome.client.domain.api.scripts.usecases.RemoveDependencyUseCase
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.presentation.scripts.addition.SetupScriptViewModel
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData

class SetupDependencyViewModel: KoinViewModel() {
    private lateinit var dependencyId: DependencyId
    private lateinit var setupScriptViewModel: SetupScriptViewModel
    private val removeDependency: RemoveDependencyUseCase by inject()
    val close = NavigationLiveData()
    
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
    }
    
    fun setFlowViewModel(setupScriptViewModel: SetupScriptViewModel) {
        this.setupScriptViewModel = setupScriptViewModel
    }
}