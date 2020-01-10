package smarthome.client.presentation.util

class NavigationEvent {
    private var consumed = false
    
    fun consume(block: NavigationEvent.() -> Unit) {
        if (!consumed) {
            consumed = true
            block()
        }
    }
}

