package smarthome.client.fragments.scriptdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import smarthome.client.viewpager.scripts.MockAction
import smarthome.client.viewpager.scripts.MockCondition
import smarthome.client.viewpager.scripts.Script

class ScriptDetailViewModel: ViewModel() {
    private val _script = MutableLiveData<Script>()
    val script: LiveData<Script>
        get() = _script


    fun setScriptGuid(guid: Long) {
        _script.value = Script("mock script", MockCondition(), MockAction())
    }
}