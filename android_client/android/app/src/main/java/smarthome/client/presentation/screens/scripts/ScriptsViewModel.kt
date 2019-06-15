package smarthome.client.presentation.screens.scripts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.domain.usecases.ScriptUseCase
import smarthome.library.common.scripts.Script

class ScriptsViewModel : ViewModel(), KoinComponent {
    private val _scripts = MutableLiveData<MutableList<Script>>()
    private val _refresh = MutableLiveData<Boolean>()
    private val scriptsDisposable: Disposable? = null

    private val scriptUseCase: ScriptUseCase by inject()

    val openScriptDetails = MutableLiveData<Script?>()

    val scripts: LiveData<MutableList<Script>>
        get() = _scripts


    val refresh: LiveData<Boolean>
        get() = _refresh

    init {
        viewModelScope.launch { scriptUseCase.getScripts().subscribe { _scripts.value = it } }
    }

    fun onRefresh() {
        _refresh.value = false
    }

    fun onScriptClick(script: Script?) {
        openScriptDetails.value = script
    }

    override fun onCleared() {
        super.onCleared()
        scriptsDisposable?.dispose()
    }

}