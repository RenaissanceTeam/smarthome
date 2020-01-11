package smarthome.client.presentation.controllers.controllerdetail

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.GetControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.entity.Controller
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.util.Data
import smarthome.client.util.ErrorStatus
import smarthome.client.util.LoadingStatus
import smarthome.client.util.log

class ControllerDetailViewModel : KoinViewModel(), LifecycleObserver {
    val refresh = MutableLiveData<Boolean>()
    val controller = MutableLiveData<Controller>()
    private var controllerId: Long = 0
    private val getControllersUseCase: GetControllerUseCase by inject()
    private val observeControllerUseCase: ObserveControllerUseCase by inject()
    private val disposable = CompositeDisposable()
    
    fun setControllerId(id: Long) {
        controllerId = id
    
        disposable.add(observeControllerUseCase.execute(id).subscribe {
            when (it) {
                is Data -> {
                    controller.postValue(it.data)
                    refresh.postValue(false)
                }
                is LoadingStatus -> refresh.postValue(true)
                is ErrorStatus -> {
                    refresh.postValue(false)
                    log(it.cause)
                }
            }
        })
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        onRefresh()
    }
    
    fun onRefresh() {
        viewModelScope.launch {
            getControllersUseCase.runCatching { execute(controllerId) }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
    
    fun controllerNameChanged(name: String) {
        TODO()
    }
}