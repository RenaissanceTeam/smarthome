package smarthome.client.presentation.util

class ConsumableEvent<T>(val param: T) {
    private var consumed = false
    
    fun consume(block: ConsumableEvent<T>.(T) -> Unit) {
        if (!consumed) {
            consumed = true
            block(param)
        }
    }
}

