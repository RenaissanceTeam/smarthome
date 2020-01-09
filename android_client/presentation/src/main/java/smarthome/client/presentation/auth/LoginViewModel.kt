package smarthome.client.presentation.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.auth.usecases.LoginUseCase
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationEvent
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.util.log

class LoginViewModel : KoinViewModel() {
    val showProgress = MutableLiveData<Boolean>(false)
    val close = NavigationLiveData()
    private val loginUseCase: LoginUseCase by inject()
    
    
    fun login(login: String, password: String) {
        viewModelScope.launch {
            showProgress.postValue(true)
            loginUseCase.runCatching { execute(login, password) }.onFailure {
                log(it)
            }
            showProgress.postValue(false)
            close.trigger()
        }
    }
}