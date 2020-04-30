package smarthome.client.presentation.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.auth.usecases.LoginUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.presentation.util.ToastLiveData
import smarthome.client.util.EmptyStatus

class LoginViewModel : KoinViewModel() {
    val showProgress = MutableLiveData<Boolean>(false)
    val errors = ToastLiveData()
    val close = NavigationLiveData()
    val openHomeServer = NavigationLiveData()

    private val observeActiveHomeServerUseCase: ObserveActiveHomeServerUseCase by inject()
    private val loginUseCase: LoginUseCase by inject()

    fun login(login: String, password: String) {
        viewModelScope.launch {
            showProgress.postValue(true)

            loginUseCase.runCatching { execute(login, password) }
                    .onSuccess { close.trigger() }
                    .onFailure { errors.post("Can't login: ${it.message}") }

            showProgress.postValue(false)
        }
    }

    fun onHomeServerClick() = openHomeServer.trigger()

    override fun onResume() {
        disposable.add(observeActiveHomeServerUseCase.execute().subscribe {
            if (it is EmptyStatus) openHomeServer.trigger()
        })
    }
}