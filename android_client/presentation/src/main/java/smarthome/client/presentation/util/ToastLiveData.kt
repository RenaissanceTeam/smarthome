package smarthome.client.presentation.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe

class ToastLiveData : MutableLiveData<ConsumableEvent<String>>() {
    fun post(value: String) = postValue(ConsumableEvent(value))
    
    fun onToast(lifecycleOwner: LifecycleOwner, action: (String) -> Unit) = observe(lifecycleOwner) {
        it.consume { action(it) }
    }
}