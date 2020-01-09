package smarthome.client.presentation.homeserver

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.homeserver.usecases.ChangeHomeServerUrlUseCase
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationEvent
import smarthome.client.presentation.util.NavigationLiveData

class HomeServerViewModel : KoinViewModel() {
    private val changeHomeServerUrlUseCase by inject<ChangeHomeServerUrlUseCase>()
    val serverUrl = MutableLiveData<String>("")
    val close = NavigationLiveData()
    
    fun save(url: String) {
        viewModelScope.launch {
            if (serverUrl.value != url) {
                changeHomeServerUrlUseCase.execute(url)
            }
    
            close.trigger()
        }
    }
}