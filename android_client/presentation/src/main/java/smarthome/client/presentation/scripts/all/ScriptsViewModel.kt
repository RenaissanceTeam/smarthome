package smarthome.client.presentation.scripts.all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import smarthome.client.domain.api.scripts.usecases.GetScriptsOverviewUseCase
import smarthome.client.presentation.scripts.all.items.ScriptsItemState
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.presentation.util.ToastLiveData
import smarthome.client.presentation.util.extensions.runInScopeLoading

class ScriptsViewModel : KoinViewModel() {

    val scripts = MutableLiveData<List<ScriptsItemState>>()
    val refresh = MutableLiveData(false)
    val openNewScript = NavigationLiveData()
    val errors = ToastLiveData()
    private val getScripts: GetScriptsOverviewUseCase by inject()


    override fun onResume() {
        onRefresh()
    }

    fun onRefresh() {
        getScripts.runInScopeLoading(viewModelScope, refresh) {
            runCatching { execute() }
                    .onFailure { errors.post("Can't load scripts: ${it.message}") }
                    .onSuccess { scripts.value = it.map { ScriptsItemState(script = it) } }

        }
    }

    fun onAddScriptClicked() {
        openNewScript.trigger()
    }

    fun notRefreshing(): Boolean {
        return refresh.value == false
    }

    fun onScriptClicked(id: Long) {

    }

    fun onEnableClicked(id: Long, enable: Boolean) {

    }
}