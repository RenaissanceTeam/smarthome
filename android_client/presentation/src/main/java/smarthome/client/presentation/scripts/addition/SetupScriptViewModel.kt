package smarthome.client.presentation.scripts.addition

import androidx.lifecycle.MutableLiveData
import smarthome.client.entity.script.Script
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData

class SetupScriptViewModel : KoinViewModel() {
    val scriptId: Long = 1L // TODO
    val scriptToAdd = MutableLiveData<Script>()
    val navigateToAddingController = NavigationLiveData()
    val finishFlow = NavigationLiveData()
    
    fun onNextFromScriptInfoClicked(name: String, description: String) {
        val script = scriptToAdd.value ?: Script()
        scriptToAdd.value = script.copy(name = name, description = description)
        
        navigateToAddingController.trigger()
    }
    
    fun onSaveClicked() {
        // todo save to server
        finishFlow.trigger()
    }
}