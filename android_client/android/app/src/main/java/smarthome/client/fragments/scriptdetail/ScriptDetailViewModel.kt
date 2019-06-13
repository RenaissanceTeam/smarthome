package smarthome.client.fragments.scriptdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import smarthome.client.*
import smarthome.client.scripts.conditions.AllConditionsProvider
import smarthome.library.common.scripts.Script
import smarthome.client.scripts.actions.ActionViewWrapper
import smarthome.client.scripts.actions.AllActionsProvider
import smarthome.client.scripts.conditions.ConditionViewWrapper
import smarthome.library.common.BaseController

class ScriptDetailViewModel: ViewModel(), AllConditionsProvider, AllActionsProvider {
    private val _script = MutableLiveData<Script>()
    val script: LiveData<Script>
        get() = _script

    val isConditionOpen = MutableLiveData<Boolean>()
    val isActionOpen = MutableLiveData<Boolean>()
    val isScriptOpen = MutableLiveData<Boolean>()
    val conditions: LiveData<MutableList<ConditionViewWrapper>> = Transformations.map(script) {
        it.conditions.map { condition -> ConditionViewWrapper.wrap(condition, this) }.toMutableList()
    }

    val actions: LiveData<MutableList<ActionViewWrapper>> = Transformations.map(script) {
        it.actions.map { action -> ActionViewWrapper.wrap(action, this) }.toMutableList()
    }

    private var copyBeforeEditCondition: Script? = null

    fun setScriptGuid(guid: Long) {
        if (guid == NEW_SCRIPT_GUID) {
            _script.value = Script()
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
        isConditionOpen.value = true
    }

    fun onEditActionClicked() {
        isActionOpen.value = true
    }

    fun onSaveConditionsClicked() {
        val conditions = conditions.value ?: return

        Log.d("ScriptDetailVM", "onSaveClicked: ${conditions.joinToString() }")
        val allFilled = conditions.isNotEmpty() && conditions.all { it.isFilled() }

        if (allFilled) {
            isConditionOpen.value = false
        }
    }


    fun onAddActionButtonClicked() {
        val script = script.value
        script ?: return

        script.actions.add(ActionViewWrapper.withTag(ACTION_READ_CONTROLLER))
        _script.value = script
    }

    fun onSaveActionsClicked() {
        val actions = actions.value ?: return

        Log.d("ScriptDetailVM", "onSaveClicked: ${actions.joinToString() }")
        val allFilled = actions.isNotEmpty() && actions.all { it.isFilled() }

        if (allFilled) {
            isActionOpen.value = false
        }
    }

    fun changeConditionType(position: Int, tag: String) {
        val script = script.value ?: return
        val conditionViewWrappers = conditions.value ?: return
        if ((conditionViewWrappers[position]).getTag() == tag) return

        script.conditions[position] = ConditionViewWrapper.withTag(tag)
        _script.value = script
    }

    fun changeActionType(position: Int, tag: String) {
        val script = script.value ?: return
        val actionViewWrappers = actions.value ?: return
        if (actionViewWrappers[position].getTag() == tag) return

        script.actions[position] = ActionViewWrapper.withTag(tag)
        _script.value = script
    }

    override suspend fun getControllers(): List<BaseController> {
        val devices = Model.getDevices()
        return devices.flatMap { it.controllers }
    }

    fun onAddConditionButtonClicked() {
        val script = script.value
        script ?: return

        script.conditions.add(ConditionViewWrapper.withTag(CONDITION_CONTROLLER))
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

    fun removeActionAt(position: Int) {
        val script = script.value
        script ?: return

        script.actions.removeAt(position)
        _script.value = script
    }
}