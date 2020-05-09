package smarthome.client.presentation.auth.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import smarthome.client.domain.api.auth.usecases.SignUpUseCase
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.presentation.util.ToastLiveData
import smarthome.client.presentation.util.extensions.runInScopeLoading

class SignUpViewModel : KoinViewModel() {
    val showProgress = MutableLiveData(false)
    val errors = ToastLiveData()
    val toDashboard = NavigationLiveData()
    val openHomeServer = NavigationLiveData()
    val toLogin = NavigationLiveData()

    private val signUpUseCase: SignUpUseCase by inject()

    fun signup(login: String, password: String, registrationCode: Long) {
        signUpUseCase.runInScopeLoading(viewModelScope, showProgress) {
            runCatching { execute(login, password, registrationCode) }
                    .onSuccess { toDashboard.trigger() }
                    .onFailure { errors.post("Can't sign up: ${it.message}") }
        }
    }

    fun onHomeServerClick() = openHomeServer.trigger()
    fun onLoginClick() = toLogin.trigger()
}
