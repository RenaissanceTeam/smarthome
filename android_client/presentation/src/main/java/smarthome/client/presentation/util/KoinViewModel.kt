package smarthome.client.presentation.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import smarthome.client.util.log

abstract class KoinViewModel : ViewModel(), KoinComponent, LifecycleObserver {
    val disposable = CompositeDisposable()
    
    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {}
    
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {}
}
