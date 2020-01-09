package smarthome.client.presentation.scripts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import smarthome.client.entity.Script


class ScriptsViewModel() : ViewModel() {
    private val _scripts = MutableLiveData<MutableList<Script>>()
    private val _refresh = MutableLiveData<Boolean>()
    private val scriptsDisposable: Disposable? = null
    val openScriptDetails = MutableLiveData<Script?>()
    val scripts: LiveData<MutableList<Script>>
        get() = _scripts
    val refresh: LiveData<Boolean>
        get() = _refresh
    
    init {
//        viewModelScope.launch { scriptUseCase.getScripts().subscribe { _scripts.value = it } }
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
