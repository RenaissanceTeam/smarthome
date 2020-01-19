package smarthome.client.presentation.scripts

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import smarthome.client.domain.api.scripts.usecases.FetchScriptsUseCase
import smarthome.client.presentation.runInScopeCatchingAny
import smarthome.client.presentation.scripts.items.ScriptsItemState
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.util.log

class ScriptsViewModel : KoinViewModel() {
    
    val scripts = MutableLiveData<List<ScriptsItemState>>()
    private val fetchScripts: FetchScriptsUseCase by inject()
    
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        onRefresh()
    }
    
    fun onRefresh() {
        fetchScripts.runInScopeCatchingAny(viewModelScope, onFailure = { log(it) }) {
            val fetched = execute()
            
            scripts.value = fetched.map { ScriptsItemState(script = it) }
        }
    }
}