package smarthome.client.presentation.main

class StateMachine(
    private val loginState: State<Boolean>,
    private val homeServerState: State<Boolean>,
    private val onNeedHomeServer: Need,
    private val onNeedLogin: Need
) {
    init {
        loginState.onChangeState { onChangeState() }
        homeServerState.onChangeState { onChangeState() }
    }
    
    private fun onChangeState() {
        if (!homeServerState.get()) onNeedHomeServer()
        if (homeServerState.get() && !loginState.get()) onNeedLogin()
    }
}

typealias Need = () -> Unit