package smarthome.client.domain.api.main

open class State<T> (default: T) {
    private var state: T = default
    private var ready = false
    
    private var onChangeStateListener: (T) -> Unit = { }
    fun set(value: T) {
        if (!ready) ready = true
        state = value
        onChangeStateListener(value)
    }
    
    fun get(): T = state
    
    fun onChangeState(listener: (T) -> Unit) {
        onChangeStateListener = listener
    }
    
    fun isReady() = ready
}