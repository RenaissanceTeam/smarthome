package smarthome.client.presentation.homeserver

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.homeserver.usecases.ChangeHomeServerUrlUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.util.data

class HomeServerViewModel : KoinViewModel() {
    private val changeHomeServerUrlUseCase by inject<ChangeHomeServerUrlUseCase>()
    private val observeHomeServerUrlUseCase: ObserveActiveHomeServerUseCase by inject()
    val serverUrl = MutableLiveData("")
    val close = NavigationLiveData()
    
    override fun onResume() {
        disposable.add(
            observeHomeServerUrlUseCase.execute().subscribe {
                it.data?.let { serverUrl.postValue(it.url) }
            }
        )
    }
    
    fun save(url: String) {
        viewModelScope.launch {
            if (serverUrl.value != url) {
                changeHomeServerUrlUseCase.execute(url)
            }
            
            close.trigger()
        }
    }
}