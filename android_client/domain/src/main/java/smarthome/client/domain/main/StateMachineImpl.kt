package smarthome.client.domain.main

import smarthome.client.domain.api.main.Need
import smarthome.client.domain.api.main.State
import smarthome.client.domain.api.main.StateMachine

class StateMachineImpl(
    private val loginState: State<Boolean>,
    private val homeServerState: State<Boolean>
): StateMachine {
    init {
        loginState.onChangeState { onChangeState() }
        homeServerState.onChangeState { onChangeState() }
    }
    
    private var onNeedHomeServer: Need = {}
    private var onNeedLogin: Need = {}
    
    override fun setOnNeedHomeServer(need: Need) {
        onNeedHomeServer = need
    }
    
    override fun setOnNeedLogin(need: Need) {
        onNeedLogin = need
    }
    
    private fun onChangeState() {
        if (!homeServerState.get()) ifReady(onNeedHomeServer, homeServerState)
        if (homeServerState.get() && !loginState.get()) ifReady(onNeedLogin, loginState)
    }
    
    private fun <T> ifReady(need: Need, state: State<T>) {
        if (state.isReady()) need()
    }
}