package smarthome.client.presentation.homeserver

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import smarthome.client.domain.api.homeserver.usecases.ChangeHomeServerUrlUseCase
import smarthome.client.presentation.util.KoinViewModel

class HomeServerViewModel : KoinViewModel() {
    private val changeHomeServerUrlUseCase by inject<ChangeHomeServerUrlUseCase>()
    val serverUrl = MutableLiveData<String>("")
    val close = MutableLiveData<Unit>()
    fun save(url: String) {
        if (serverUrl.value != url) {
            changeHomeServerUrlUseCase.execute(url)
        }
        
        close.value = Unit
    }
}