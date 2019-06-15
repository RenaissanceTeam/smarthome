package smarthome.client.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.presentation.ui.AuthUIWrapper
import smarthome.client.domain.usecases.AuthenticationUseCase

class MainViewModel(application: Application) : AndroidViewModel(application), KoinComponent {

    private val TAG = MainViewModel::class.java.simpleName

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean>
        get() = _isAuthenticated

    private val authenticationUseCase: AuthenticationUseCase by inject()

    val authUiWrapper = AuthUIWrapper
    private val authDisposable: Disposable

    init {
        authDisposable = authenticationUseCase.getAuthenticationStatus().subscribe { _isAuthenticated.value = it}
    }

    fun onAuthSuccessful() {
        authenticationUseCase.onAuthSuccess()
    }

    fun onAuthFailed() {
        authenticationUseCase.onAuthFail()
    }

    override fun onCleared() {
        super.onCleared()

        authDisposable.dispose()
    }
}