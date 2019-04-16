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

    val isConditionOpen = MutableLiveData<Boolean>()
    val isScriptOpen = MutableLiveData<Boolean>()
    val conditions:LiveData<MutableList<Condition>> = Transformations.map(script) { it.conditions }
    val actions = Transformations.map(script) { it.actions }

    private var copyBeforeEditCondition: Script? = null

    fun setScriptGuid(guid: Long) {
        if (guid == INVALID_GUID) {
            _script.value = Script("",
                    mutableListOf(),
                    mutableListOf(MockAction()))
        } else {
            _script.value = Model.getScript(guid)
        }
    }

    fun scriptNameChange(name: String) {
        val oldScript = _script.value ?: return
        _script.value = Script(name, oldScript.conditions, oldScript.actions)

        // todo save to firestore
    }

    fun onEditConditionClicked() {
        copyBeforeEditCondition = _script.value?.copy()
        isConditionOpen.value = true
    }

    fun onEditActionClicked() {

    }

    fun onSaveConditionsClicked() {
        val conditions = script.value?.conditions ?: return

        Log.d("ScriptDetailVM", "onSaveClicked: ${conditions.joinToString() }")
        val allFilled = conditions.isNotEmpty() && conditions.all { it.isFilled() }

        if (allFilled) {
            isConditionOpen.value = false
        }
    }

    fun onDiscardConditionsChanges() {
        _script.value = copyBeforeEditCondition
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

    fun onSaveScriptClicked() {
        val script = script.value ?: return
        if (script.name.isNotEmpty() && script.conditions.isNotEmpty() && script.actions.isNotEmpty()) {
            isScriptOpen.value = false
            Model.saveScript(script)
        } else {
            // todo show not filled fields
        }

    }

    fun onCreateScriptDetails() {
        isScriptOpen.value = true
    }
}