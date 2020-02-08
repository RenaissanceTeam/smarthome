package smarthome.client.presentation.scripts.all

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import smarthome.client.domain.api.scripts.usecases.FetchScriptsUseCase
import smarthome.client.entity.script.Script
import smarthome.client.util.runInScope
import smarthome.client.presentation.scripts.all.items.ScriptsItemState
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.util.log

class ScriptsViewModel : KoinViewModel() {

    val scripts = MutableLiveData<List<ScriptsItemState>>()
    val refresh = MutableLiveData<Boolean>()
    val openAddition = NavigationLiveData()
    private val fetchScripts: FetchScriptsUseCase by inject()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        onRefresh()
    }

    fun onRefresh() {
        fetchScripts.runInScope(viewModelScope) {
            refresh.value = true

            runCatching {
                execute()
            }.onFailure {
                log(it)
            }.onSuccess { it: List<Script> ->
                scripts.value = it.map { ScriptsItemState(script = it) }
            }

            refresh.value = false
        }
    }
    
    fun onAddScriptClicked() {
        openAddition.trigger()
    }

    fun notRefreshing(): Boolean {
        return refresh.value == false
    }
}