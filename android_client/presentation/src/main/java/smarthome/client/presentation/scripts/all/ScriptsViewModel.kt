package smarthome.client.presentation.scripts.all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import smarthome.client.domain.api.scripts.RemoveScriptUseCase
import smarthome.client.domain.api.scripts.usecases.GetScriptsOverviewUseCase
import smarthome.client.domain.api.scripts.usecases.SetScriptEnabledUseCase
import smarthome.client.entity.NOT_DEFINED_ID
import smarthome.client.presentation.scripts.all.items.ScriptsItemState
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationParamLiveData
import smarthome.client.presentation.util.ToastLiveData
import smarthome.client.presentation.util.extensions.runInScopeLoading
import smarthome.client.presentation.util.extensions.triggerRebuild
import smarthome.client.presentation.util.extensions.updateWith
import smarthome.client.util.findAndModify
import smarthome.client.util.runInScope
import smarthome.client.util.withRemoved

class ScriptsViewModel : KoinViewModel() {

    val scripts = MutableLiveData<List<ScriptsItemState>>()
    val refresh = MutableLiveData(false)
    val openSetup = NavigationParamLiveData<Long>()
    val errors = ToastLiveData()
    private val getScripts: GetScriptsOverviewUseCase by inject()
    private val removeScriptUseCase: RemoveScriptUseCase by inject()
    private val setScriptEnabledUseCase: SetScriptEnabledUseCase by inject()


    override fun onResume() {
        onRefresh()
    }

    fun onRefresh() {
        getScripts.runInScopeLoading(viewModelScope, refresh) {
            runCatching { execute() }
                    .onFailure { errors.post("Can't load scripts: ${it.message}") }
                    .onSuccess { scripts.postValue(it.map { ScriptsItemState(script = it) }) }
        }
    }

    fun onAddScriptClicked() {
        openSetup.trigger(NOT_DEFINED_ID)
    }

    fun notRefreshing(): Boolean {
        return refresh.value == false
    }

    fun onScriptClicked(id: Long) {
        openSetup.trigger(id)
    }

    fun onEnableClicked(id: Long, enable: Boolean) {
        runInScope(viewModelScope) {
            updateScriptModel(id) { it.copy(enableInProgress = true) }

            runCatching { setScriptEnabledUseCase.execute(id, enable) }
                    .onSuccess {
                        updateScriptModel(id) {
                            it.copy(script = it.script.copy(enabled = enable))
                        }
                    }

            updateScriptModel(id) { it.copy(enableInProgress = false) }
        }
    }

    private fun updateScriptModel(id: Long, partialUpdate: (ScriptsItemState) -> ScriptsItemState) {
        scripts.updateWith { scripts ->
            scripts ?: return@updateWith scripts
            scripts.findAndModify({ it.script.id == id }, partialUpdate)
        }
    }

    fun onRemove(scriptId: Long) {
        runInScopeLoading(viewModelScope, refresh) {
            kotlin.runCatching { removeScriptUseCase.execute(scriptId) }
                    .onSuccess {
                        scripts.updateWith { it?.withRemoved { it.script.id == scriptId } }
                    }
                    .onFailure {
                        errors.post("Can't remove: $it")
                        scripts.triggerRebuild()
                    }
        }
    }
}