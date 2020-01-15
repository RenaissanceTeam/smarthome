package smarthome.client.presentation.controllers.controllerdetail

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.GetControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.entity.Controller
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.util.*

class ControllerDetailViewModel : KoinViewModel() {
    val refresh = MutableLiveData<Boolean>()
    val controller = MutableLiveData<Controller>()
    private var controllerId: Long = 0
    private val getControllersUseCase: GetControllerUseCase by inject()
    private val observeControllerUseCase: ObserveControllerUseCase by inject()
    
    fun setControllerId(id: Long) {
        controllerId = id
    
        disposable.add(observeControllerUseCase.execute(id).subscribe {
            when (it) {
                is Data -> {
                    controller.postValue(it.data)
                    refresh.postValue(false)
                }
                is LoadingStatus -> {
                    it.data?.let(controller::postValue)
                    refresh.postValue(true)
                }
                is ErrorStatus -> {
                    it.data?.let(controller::postValue)
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
    
    fun controllerNameChanged(name: String) {
        TODO()
    }
}