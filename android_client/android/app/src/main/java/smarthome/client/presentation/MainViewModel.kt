package smarthome.client.presentation

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.App
import smarthome.client.domain.usecases.AuthenticationUseCase

class MainViewModel(application: Application) : AndroidViewModel(application), KoinComponent {
    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean>
        get() = _isAuthenticated

    private val authenticationUseCase: AuthenticationUseCase by inject()
    private val providers = listOf(AuthUI.IdpConfig.GoogleBuilder().build())
    private val authDisposable: Disposable

    init {
        authDisposable = authenticationUseCase.getAuthenticationStatus().subscribe { _isAuthenticated.value = it}
    }

    fun getAuthIntent(): Intent {
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        AuthUI.getInstance().delete(App.appContext)
        AuthUI.getInstance().signOut(App.appContext)
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