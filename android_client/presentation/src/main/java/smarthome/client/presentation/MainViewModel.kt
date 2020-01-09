package smarthome.client.presentation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.inject
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.domain.api.usecase.AuthenticationUseCase
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.navigateIf
import smarthome.client.util.DATA

class MainViewModel : KoinViewModel(), LifecycleObserver {
    private val authenticationUseCase: AuthenticationUseCase by inject()
    private val observeActiveHomeServerUseCase: ObserveActiveHomeServerUseCase by inject()
    
    val isAuthenticated = MutableLiveData<Boolean>()
    val hasHomeServer = MutableLiveData<Boolean>(true)
    val openLogin = isAuthenticated.navigateIf { isAuthenticated ->
        val needAuth = isAuthenticated?.not() ?: false
        val serverSet = hasHomeServer.value ?: false
        
        return@navigateIf needAuth && serverSet
    }
    val openHomeServerSetup = hasHomeServer.navigateIf { it?.not() ?: false }
    
    private val toDispose = CompositeDisposable()
    
    override fun onCleared() {
        super.onCleared()
        
        toDispose.dispose()
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        toDispose.add(authenticationUseCase.getAuthenticationStatus().subscribe {
            isAuthenticated.value = it
        })
        toDispose.addAll(observeActiveHomeServerUseCase.execute().subscribe {
            hasHomeServer.value = (it.status == DATA)
        })
    }
}