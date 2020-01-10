package smarthome.client.presentation.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe

class NavigationLiveData : MutableLiveData<NavigationEvent>() {
    fun trigger() {
        postValue(NavigationEvent())
    }
    
    fun onNavigate(lifecycleOwner: LifecycleOwner, action: () -> Unit) {
        observe(lifecycleOwner) {
            action()
        }
    }
}