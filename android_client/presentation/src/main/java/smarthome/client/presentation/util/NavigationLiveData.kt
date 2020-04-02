package smarthome.client.presentation.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe

class NavigationParamLiveData<T> : MutableLiveData<ConsumableEvent<T>>() {
    fun trigger(param: T) {
        postValue(ConsumableEvent(param))
    }
    
    fun onNavigate(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) {
        observe(lifecycleOwner) {
            it.consume { param -> action(param) }
        }
    }
}

class NavigationLiveData : MutableLiveData<ConsumableEvent<Unit>>() {
    fun trigger() {
        postValue(ConsumableEvent(Unit))
    }
    
    fun onNavigate(lifecycleOwner: LifecycleOwner, action: () -> Unit) {
        observe(lifecycleOwner) {
            it.consume { action() }
        }
    }
}

