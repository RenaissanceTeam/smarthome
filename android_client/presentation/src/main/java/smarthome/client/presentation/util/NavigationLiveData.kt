package smarthome.client.presentation.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe

class NavigationParamLiveData<T> : MutableLiveData<NavigationEvent<T>>() {
    fun trigger(param: T) {
        postValue(NavigationEvent(param))
    }
    
    fun onNavigate(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) {
        observe(lifecycleOwner) {
            it.consume { param -> action(param) }
        }
    }
}

class NavigationLiveData: MutableLiveData<NavigationEvent<Unit>>() {
    fun trigger() {
        postValue(NavigationEvent(Unit))
    }
    
    fun onNavigate(lifecycleOwner: LifecycleOwner, action: () -> Unit) {
        observe(lifecycleOwner) {
            it.consume { action() }
        }
    }
}