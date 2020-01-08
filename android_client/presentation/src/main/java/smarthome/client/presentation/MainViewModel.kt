package smarthome.client.presentation

import androidx.lifecycle.*
import com.snakydesign.livedataextensions.filter
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.homeserver.usecases.GetHomeServerUseCase
import smarthome.client.domain.api.usecase.AuthenticationUseCase
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationEvent
import smarthome.client.presentation.util.navigateIf

class MainViewModel: KoinViewModel(), LifecycleObserver {
    private val authenticationUseCase: AuthenticationUseCase by inject()
    private val getHomeServerUseCase: GetHomeServerUseCase by inject()
    
    val isAuthenticated = MutableLiveData<Boolean>()
    val hasHomeServer = MutableLiveData<Boolean>(true)
    val openLogin = isAuthenticated.navigateIf { isAuthenticated ->
        val needAuth = isAuthenticated?.not() ?: false
        val serverSet = hasHomeServer.value ?: false
        
        return@navigateIf needAuth && serverSet
    }
    val openHomeServerSetup = hasHomeServer.navigateIf { it?.not() ?: false }
    
    private lateinit var authDisposable: Disposable
    
    override fun onCleared() {
        super.onCleared()
        
        authDisposable.dispose()
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        runCatching { getHomeServerUseCase.execute() }.onFailure { hasHomeServer.value = false }
    
        authDisposable =
            authenticationUseCase.getAuthenticationStatus().subscribe { isAuthenticated.value = it }
    }
}