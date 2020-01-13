package smarthome.client.presentation.main

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import org.koin.core.inject
import org.koin.core.qualifier.named
import smarthome.client.domain.api.auth.usecases.ObserveAuthenticationStatusUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.domain.api.main.BooleanState
import smarthome.client.domain.api.main.StateMachine
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.util.Data

class MainViewModel : KoinViewModel(), LifecycleObserver {
    private val observeAuthenticationStatusUseCase: ObserveAuthenticationStatusUseCase by inject()
    private val observeActiveHomeServerUseCase: ObserveActiveHomeServerUseCase by inject()
    private val stateMachine: StateMachine by inject()
    private val loginState: BooleanState by inject(named("login"))
    private val homeServerState: BooleanState by inject(named("homeServer"))
    
    val openLogin =  NavigationLiveData()
    val openHomeServerSetup = NavigationLiveData()
    
    init {
        stateMachine.setOnNeedLogin { openLogin.trigger() }
        stateMachine.setOnNeedHomeServer { openHomeServerSetup.trigger() }
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        disposable.add(observeAuthenticationStatusUseCase.execute().subscribe {
            loginState.set(it)
        })
    
        disposable.addAll(observeActiveHomeServerUseCase.execute().subscribe {
            homeServerState.set(it is Data)
        })
    }
}
