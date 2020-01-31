package smarthome.client.domain.api.main

interface StateMachine {
    fun setOnNeedHomeServer(need: Need)
    fun setOnNeedLogin(need: Need)
}


typealias Need = () -> Unit