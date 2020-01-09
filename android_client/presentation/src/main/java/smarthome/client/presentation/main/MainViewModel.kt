package smarthome.client.presentation.main

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.inject
import smarthome.client.domain.api.auth.usecases.ObserveAuthenticationStatusUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationEvent
import smarthome.client.util.DATA

class MainViewModel : KoinViewModel(), LifecycleObserver {
    private val observeAuthenticationStatusUseCase: ObserveAuthenticationStatusUseCase by inject()
    private val observeActiveHomeServerUseCase: ObserveActiveHomeServerUseCase by inject()
    
    val openLogin = MutableLiveData<NavigationEvent>()
    val openHomeServerSetup = MutableLiveData<NavigationEvent>()
    
    private val toDispose = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        toDispose.add(observeAuthenticationStatusUseCase.execute().subscribe {
//            isAuthenticated.postValue(it)
        })
        
        toDispose.addAll(observeActiveHomeServerUseCase.execute().subscribe {
            val hasHome = it.status == DATA
//            hasHomeServer.postValue(hasHome)
//            if (hasHome && isAuthenticated.value == false) openLogin
        })
    }
    
    override fun onCleared() {
        super.onCleared()
        toDispose.dispose()
    }
    
    
}

