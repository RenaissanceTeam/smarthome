package smarthome.client.presentation.settings

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import smarthome.client.presentation.util.KoinViewModel

class SettingsViewModel : KoinViewModel() {

    fun signOut() = viewModelScope.launch {

    }
}