package smarthome.client.presentation.scripts.addition

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import org.koin.ext.getOrCreateScope
import org.koin.ext.scope
import smarthome.client.domain.api.scripts.usecases.setup.*
import smarthome.client.entity.NOT_DEFINED_ID
import smarthome.client.entity.script.Script
import smarthome.client.entity.script.ScriptInfo
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.presentation.util.extensions.runInScopeLoading
import smarthome.client.util.log
import smarthome.client.util.logObject

class SetupScriptViewModel : KoinViewModel() {
    val setupScript = MutableLiveData<Script>()
    val loading = MutableLiveData(false)
    val navigateToAddingController = NavigationLiveData()
    val close = NavigationLiveData()
    val finishFlow = NavigationLiveData()
    private val startSetupScriptUseCase: StartSetupScriptUseCase by inject()
    private val updateScriptInfoUseCase: UpdateScriptInfoUseCase by inject()
    private val saveSetupScriptUseCase: SaveSetupScriptUseCase by inject()
    private val observeSetupScriptUseCase: ObserveSetupScriptUseCase by inject()
    private val cancelSetupScriptUseCase: CancelSetupScriptUseCase by inject()
    
    init {
        disposable.add(observeSetupScriptUseCase.execute().subscribe { script ->
            setupScript.value = script
        })
    }
    
    fun onNextFromScriptInfoClicked(name: String, description: String) {
        updateScriptInfoUseCase.execute(ScriptInfo(name, description))
        navigateToAddingController.trigger()
    }
    
    fun onSaveClicked() {
        saveSetupScriptUseCase.runInScopeLoading(viewModelScope, loading) {
            execute()
            finishFlow.trigger()
        }
    }
    
    fun onScriptId(id: Long) {
        if (setupScript.value != null) return
        
        startSetupScriptUseCase.runInScopeLoading(viewModelScope, loading) {
            runCatching { execute(id.takeUnless { it == NOT_DEFINED_ID }) }
        }
    }
    
    
    fun onCancel() {
        cancelSetupScriptUseCase.execute()
        close.trigger()
        "setup".scope.close()
    }
}