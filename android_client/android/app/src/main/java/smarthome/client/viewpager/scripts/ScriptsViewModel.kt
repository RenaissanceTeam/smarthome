package smarthome.client.viewpager.scripts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import smarthome.client.Model
import smarthome.client.scripts.commonlib.scripts.Script

class ScriptsViewModel : ViewModel() {
    private val _scripts = MutableLiveData<MutableList<Script>>()
    private val _refresh = MutableLiveData<Boolean>()
    private val scriptsDisposable: Disposable? = null
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    val openScriptDetails = MutableLiveData<Script?>()

    val scripts: LiveData<MutableList<Script>>
        get() = _scripts


    val refresh: LiveData<Boolean>
        get() = _refresh

    init {
        uiScope.launch { Model.getScriptsObservable().subscribe { _scripts.value = it } }
    }

    fun onRefresh() {
        _refresh.value = false
    }

    fun onScriptClick(script: Script?) {
        openScriptDetails.value = script
    }

    override fun onCleared() {
        super.onCleared()

        job.cancel()
        scriptsDisposable?.dispose()
    }

}