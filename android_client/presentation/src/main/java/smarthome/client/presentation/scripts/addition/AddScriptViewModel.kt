package smarthome.client.presentation.scripts.addition

import androidx.lifecycle.MutableLiveData
import smarthome.client.entity.Script
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData

class AddScriptViewModel : KoinViewModel() {
    val scriptToAdd = MutableLiveData<Script>()
    val navigateToAddingController = NavigationLiveData()
    
    fun onNextFromScriptInfoClicked(name: String, description: String) {
        val script = scriptToAdd.value ?: Script()
        scriptToAdd.value = script.copy(name = name, description = description)
        
        navigateToAddingController.trigger()
    }
}