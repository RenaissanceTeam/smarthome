package smarthome.client.presentation.scripts.scriptdetail

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.domain.usecases.ControllersUseCase
import smarthome.client.domain.usecases.DevicesUseCase
import smarthome.client.domain.usecases.ScriptUseCase
import smarthome.client.presentation.ACTION_READ_CONTROLLER
import smarthome.client.presentation.NEW_SCRIPT_GUID
import smarthome.client.presentation.scripts.actions.ActionViewWrapper
import smarthome.client.presentation.scripts.actions.AllActionsProvider
import smarthome.client.presentation.scripts.conditions.AllConditionsProvider



class ScriptDetailViewModel: ViewModel(), AllConditionsProvider, AllActionsProvider, KoinComponent {
    private val _script = MutableLiveData<Script>()
    val script: LiveData<Script>
        get() = _script

    val isConditionOpen = MutableLiveData<Boolean>()
    val isActionOpen = MutableLiveData<Boolean>()
    val isScriptOpen = MutableLiveData<Boolean>()
//    val conditions: LiveData<MutableList<ConditionViewWrapper>> = Transformations.map(script) {
//        it.conditions.map { condition -> ConditionViewWrapper.wrap(condition, this) }.toMutableList()
//    }

    val actions: LiveData<MutableList<ActionViewWrapper>> = Transformations.map(script) {
        it.actions.map { action -> ActionViewWrapper.wrap(action, this) }.toMutableList()
    }

    private var copyBeforeEditCondition: Script? = null
    private val scriptUseCase: ScriptUseCase by inject()
    private val devicesUseCase: DevicesUseCase by inject()
    private val controllersUseCase: ControllersUseCase by inject()


    fun setScriptGuid(guid: Long) {
        if (guid == NEW_SCRIPT_GUID) {
            _script.value = Script()
        } else {
            viewModelScope.launch { _script.value = scriptUseCase.getScript(guid) }
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
//        val conditions = conditions.value ?: return
//
//        Log.d("ScriptDetailVM", "onSaveClicked: ${conditions.joinToString() }")
//        val allFilled = conditions.isNotEmpty() && conditions.all { it.isFilled() }
//
//        if (allFilled) {
//            isConditionOpen.value = false
//        }
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
//        val script = script.value ?: return
//        val conditionViewWrappers = conditions.value ?: return
//        if ((conditionViewWrappers[position]).getTag() == tag) return
//
//        script.conditions[position] = ConditionViewWrapper.withTag(tag)
//        _script.value = script
    }

    fun changeActionType(position: Int, tag: String) {
        val script = script.value ?: return
        val actionViewWrappers = actions.value ?: return
        if (actionViewWrappers[position].getTag() == tag) return

        script.actions[position] = ActionViewWrapper.withTag(tag)
        _script.value = script
    }

    override suspend fun getControllers(): List<Controller> {
        return controllersUseCase.getControllers()
    }

    fun onAddConditionButtonClicked() {
//        val script = script.value
//        script ?: return
//
//        script.conditions.add(ConditionViewWrapper.withTag(CONDITION_CONTROLLER))
//        _script.value = script
    }

    fun removeConditionAt(position: Int) {
        val script = script.value
        script ?: return

        script.conditions.removeAt(position)
        _script.value = script
    }

    fun onSaveScriptClicked() {
        val script = script.value ?: return


        viewModelScope.launch {
            if (script.name.isNotEmpty() && script.conditions.isNotEmpty() && script.actions.isNotEmpty()) { // todo move to use case
            isScriptOpen.value = false
            scriptUseCase.saveScript(script)
        } else {
            // todo show not filled fields
            }
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