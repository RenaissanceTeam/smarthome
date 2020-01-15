package smarthome.client.presentation.util

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent

abstract class KoinViewModel : ViewModel(), KoinComponent, LifecycleObserver {
    val disposable = CompositeDisposable()
    
    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}