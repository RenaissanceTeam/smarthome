package smarthome.client.fragments.scriptdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import smarthome.client.*
import smarthome.client.scripts.conditions.AllConditionsProvider
import smarthome.client.scripts.conditions.Condition
import smarthome.client.scripts.actions.MockAction
import smarthome.client.scripts.Script
import smarthome.library.common.BaseController

class ScriptDetailViewModel: ViewModel(), AllConditionsProvider {
    private val _script = MutableLiveData<Script>()
    val script: LiveData<Script>
        get() = _script

    val conditions = Transformations.map(script) { it.conditions }
    val actions = Transformations.map(script) { it.actions }

    fun setScriptGuid(guid: Long) {
        _script.value = Script("Garage Light",
                mutableListOf(
                        Condition.withTag(CONDITION_EXACT_TIME, this),
                        Condition.withTag(CONDITION_CONTROLLER, this)
                ),
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
        conditions[position] = Condition.withTag(tag, this)

        _script.value = script
    }

    override suspend fun getControllers(): List<BaseController> {
        val devices = Model.getDevices()
        return devices.flatMap { it.controllers }
    }

    fun onAddButtonClicked() {
        val script = script.value
        script ?: return

        script.conditions.add(Condition.withTag(CONDITION_CONTROLLER, this))
        _script.value = script
    }

    fun removeConditionAt(position: Int) {
        val script = script.value
        script ?: return

        script.conditions.removeAt(position)
        _script.value = script
    }
}