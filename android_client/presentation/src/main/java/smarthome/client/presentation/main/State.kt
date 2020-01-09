package smarthome.client.presentation.main

open class State<T> (default: T) {
    private var state: T = default
    private var onChangeStateListener: (T) -> Unit = { }
    fun set(value: T) {
        state = value
        onChangeStateListener(value)
    }
    
    fun get(): T = state
    
    fun onChangeState(listener: (T) -> Unit) {
        onChangeStateListener = listener
    }
}