package smarthome.client.presentation.scripts.addition

import androidx.lifecycle.MutableLiveData
import smarthome.client.entity.script.Script
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData

class SetupScriptViewModel : KoinViewModel() {
    val scriptId: Long = 1L // TODO
    val setupScript = MutableLiveData<Script>()
    val navigateToAddingController = NavigationLiveData()
    val finishFlow = NavigationLiveData()
    
    fun onNextFromScriptInfoClicked(name: String, description: String) {
        val script = setupScript.value ?: Script()
        setupScript.value = script.copy(name = name, description = description)
        
        navigateToAddingController.trigger()
    }
    
    fun onSaveClicked() {
        // todo save to server
        finishFlow.trigger()
    }
}