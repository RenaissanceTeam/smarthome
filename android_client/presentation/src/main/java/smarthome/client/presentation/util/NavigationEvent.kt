package smarthome.client.presentation.util

class NavigationEvent<T>(private val param: T) {
    private var consumed = false
    
    fun consume(block: NavigationEvent<T>.(T) -> Unit) {
        if (!consumed) {
            consumed = true
            block(param)
        }
    }
}

