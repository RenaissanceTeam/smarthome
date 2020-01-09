package smarthome.client.presentation.main

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.inject
import org.koin.core.qualifier.named
import smarthome.client.domain.api.main.StateMachine
import smarthome.client.domain.api.auth.usecases.ObserveAuthenticationStatusUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.domain.api.main.BooleanState
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationEvent
import smarthome.client.util.DATA
import smarthome.client.util.log

class MainViewModel : KoinViewModel(), LifecycleObserver {
    private val observeAuthenticationStatusUseCase: ObserveAuthenticationStatusUseCase by inject()
    private val observeActiveHomeServerUseCase: ObserveActiveHomeServerUseCase by inject()
    private val stateMachine: StateMachine by inject()
    private val loginState: BooleanState by inject(named("login"))
    private val homeServerState: BooleanState by inject(named("homeServer"))
    
    val openLogin = MutableLiveData<NavigationEvent>()
    val openHomeServerSetup = MutableLiveData<NavigationEvent>()
    
    private val toDispose = CompositeDisposable()
    init {
        stateMachine.setOnNeedLogin { openLogin.postValue(NavigationEvent()) }
        stateMachine.setOnNeedHomeServer { openHomeServerSetup.postValue(NavigationEvent()) }
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        toDispose.add(observeAuthenticationStatusUseCase.execute().subscribe {
            loginState.set(it)
        })
        
        toDispose.addAll(observeActiveHomeServerUseCase.execute().subscribe {
            val hasHome = it.status == DATA
            homeServerState.set(hasHome)
        })
    }
    
    override fun onCleared() {
        super.onCleared()
        toDispose.dispose()
    }
}
