package smarthome.client.presentation.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.snakydesign.livedataextensions.filter

class NavigationEvent {
    private var consumed = false
    
    fun consume(block: NavigationEvent.() -> Unit) {
        if (!consumed) {
            consumed = true
            block()
        }
    }
}

inline fun <T> LiveData<T>.navigateIf(crossinline predicate : (T?)->Boolean): LiveData<NavigationEvent> {
    return this.filter(predicate).map { NavigationEvent() }
}
