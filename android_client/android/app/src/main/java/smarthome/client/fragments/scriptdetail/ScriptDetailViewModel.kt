package smarthome.client.fragments.scriptdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import smarthome.client.MockAction
import smarthome.client.MockCondition
import smarthome.client.Script

class ScriptDetailViewModel: ViewModel() {
    private val _script = MutableLiveData<Script>()
    val script: LiveData<Script>
        get() = _script

    val conditions = Transformations.map(script) { it.conditions }
    val actions = Transformations.map(script) { it.actions }


    fun setScriptGuid(guid: Long) {
        _script.value = Script("Garage Light", mutableListOf(MockCondition()), mutableListOf(MockAction()))
    }

    fun scriptNameChange(name: String) {
        val oldScript = _script.value ?: return
        _script.value = Script(name, oldScript.conditions, oldScript.actions)

        // todo save to firestore
    }

    fun onEditConditionClicked() {

    }

    fun onEditActionClicked() {

    }

    fun onSaveClicked() {

    }
}