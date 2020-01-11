package smarthome.client.presentation.controllers.controllerdetail

import androidx.lifecycle.*
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.GetControllerUseCase
import smarthome.client.entity.Controller
import smarthome.client.presentation.controllers.controllerdetail.statechanger.StateChangerType
import smarthome.client.presentation.util.KoinViewModel


class ControllerDetailViewModel : KoinViewModel(), LifecycleObserver {
    val refresh = MutableLiveData<Boolean>()
    val controller = MutableLiveData<Controller>()
    private var controllerId: Long = 0
    private val getControllersUseCase: GetControllerUseCase by inject()
    
    private val _stateChangerType = MutableLiveData<StateChangerType>()
    
    fun setControllerId(id: Long) {
        controllerId = id
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        onRefresh()
    }
    
    fun onRefresh() {
        viewModelScope.launch {
            refresh.postValue(true)
            getControllersUseCase.runCatching { execute(controllerId) }
                .onSuccess(controller::postValue)
            refresh.postValue(false)
        }
    }
    
    val stateChangerType: LiveData<StateChangerType>
        get() = _stateChangerType
    
    private var disposable: Disposable? = null
    
    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
    
    fun controllerNameChanged(name: String) {
        TODO()
    }
}