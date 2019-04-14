package smarthome.client.fragments.scriptdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import smarthome.client.*

class ScriptDetailViewModel: ViewModel() {
    private val _script = MutableLiveData<Script>()
    val script: LiveData<Script>
        get() = _script

    val conditions = Transformations.map(script) { it.conditions }
    val actions = Transformations.map(script) { it.actions }


    fun setScriptGuid(guid: Long) {
        _script.value = Script("Garage Light",
                mutableListOf(ControllerCondition(), ExactTimeCondition()),
                mutableListOf(MockAction()))
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

    fun onSaveConditionsClicked() {
        Log.d("ScriptDetailVM", "onSaveClicked: ${script.value?.conditions?.joinToString() }")
    }

    fun changeConditionType(position: Int, tag: String) {
        val script = script.value
        script ?: return

        val conditions = script.conditions

        if (conditions[position].getTag() == tag) return
        conditions[position] = Condition.withTitle(tag)

        _script.value = script
    }
}