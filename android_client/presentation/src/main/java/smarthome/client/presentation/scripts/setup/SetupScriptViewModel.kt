package smarthome.client.presentation.scripts.setup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import smarthome.client.domain.api.scripts.usecases.setup.*
import smarthome.client.entity.NOT_DEFINED_ID
import smarthome.client.entity.script.Script
import smarthome.client.entity.script.ScriptInfo
import smarthome.client.presentation.scripts.setup.di.setupScope
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.presentation.util.ToastLiveData
import smarthome.client.presentation.util.extensions.runInScopeLoading

class SetupScriptViewModel : KoinViewModel() {
    val setupScript = MutableLiveData<Script>()
    val loading = MutableLiveData(false)
    val navigateToAddingController = NavigationLiveData()
    val close = NavigationLiveData()
    val finishFlow = NavigationLiveData()
    val errors = ToastLiveData()
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
            runCatching { execute() }
                    .onSuccess { onSaved() }
                    .onFailure { errors.post("Can't save: ${it.message}") }
        }
    }

    fun onScriptId(id: Long) {
        if (setupScript.value != null) return

        startSetupScriptUseCase.runInScopeLoading(viewModelScope, loading) {
            runCatching { execute(id.takeUnless { it == NOT_DEFINED_ID }) }
                    .onFailure { onCancel() }
        }
    }

    fun onCancel() {
        cancelSetupScriptUseCase.execute()
        close.trigger()
        setupScope.close()
    }

    private fun onSaved() {
        setupScope.close()
        finishFlow.trigger()
    }
}