package smarthome.client.presentation.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import smarthome.client.domain.api.auth.usecases.LoginUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.presentation.util.ToastLiveData
import smarthome.client.presentation.util.extensions.runInScopeLoading
import smarthome.client.util.EmptyStatus

class LoginViewModel : KoinViewModel() {
    val showProgress = MutableLiveData(false)
    val errors = ToastLiveData()
    val close = NavigationLiveData()
    val openHomeServer = NavigationLiveData()
    val toSignUp = NavigationLiveData()

    private val observeActiveHomeServerUseCase: ObserveActiveHomeServerUseCase by inject()
    private val loginUseCase: LoginUseCase by inject()

    fun login(login: String, password: String) {
        loginUseCase.runInScopeLoading(viewModelScope, showProgress) {
            runCatching { execute(login, password) }
                    .onSuccess { close.trigger() }
                    .onFailure { errors.post("Can't login: ${it.message}") }

        }
    }

    fun onHomeServerClick() = openHomeServer.trigger()
    fun onSignUpClick() = toSignUp.trigger()

    override fun onResume() {
        disposable.add(observeActiveHomeServerUseCase.execute().subscribe {
            if (it is EmptyStatus) openHomeServer.trigger()
        })
    }
}