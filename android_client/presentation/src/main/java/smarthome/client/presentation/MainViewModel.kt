package smarthome.client.presentation

import androidx.lifecycle.*
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.homeserver.usecases.GetHomeServerUseCase
import smarthome.client.domain.api.usecase.AuthenticationUseCase
import smarthome.client.presentation.util.KoinViewModel

class MainViewModel: KoinViewModel(), LifecycleObserver {
    private val authenticationUseCase: AuthenticationUseCase by inject()
    private val getHomeServerUseCase: GetHomeServerUseCase by inject()
    
    val isAuthenticated = MutableLiveData<Boolean>()
    val hasHomeServer = MutableLiveData<Boolean>(true)
    private val authDisposable: Disposable

    init {
        authDisposable =
            authenticationUseCase.getAuthenticationStatus().subscribe { isAuthenticated.value = it }
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
    
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        runCatching { getHomeServerUseCase.execute() }.onFailure { hasHomeServer.value = false }
    }
}