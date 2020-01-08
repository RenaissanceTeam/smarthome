package smarthome.client.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import smarthome.client.domain.api.usecase.AuthenticationUseCase

class MainViewModel(
    private val authenticationUseCase: AuthenticationUseCase
): ViewModel() {
    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean>
        get() = _isAuthenticated
    private val authDisposable: Disposable

    init {
        authDisposable = authenticationUseCase.getAuthenticationStatus().subscribe { _isAuthenticated.value = it}
    }
    fun onAuthSuccessful() {
        viewModelScope.launch { authenticationUseCase.onAuthSuccess() }
    }

    fun onAuthFailed() {
        viewModelScope.launch { authenticationUseCase.onAuthFail() }
    }

    override fun onCleared() {
        super.onCleared()

        authDisposable.dispose()
    }
}