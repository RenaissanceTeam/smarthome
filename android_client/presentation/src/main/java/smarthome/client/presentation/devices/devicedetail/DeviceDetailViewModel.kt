package smarthome.client.presentation.devices.devicedetail

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.GetControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.ReadControllerUseCase
import smarthome.client.domain.api.devices.usecase.GetDeviceUseCase
import smarthome.client.entity.Controller
import smarthome.client.entity.Device
import smarthome.client.presentation.runInScope
import smarthome.client.presentation.runInScopeCatchingAny
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationParamLiveData
import smarthome.client.util.DataStatus
import smarthome.client.util.EmptyStatus
import kotlin.collections.set


class DeviceDetailViewModel : KoinViewModel() {
    val device = MutableLiveData<Device>()
    val controllersLiveData = MutableLiveData<List<DataStatus<Controller>>>()
    val refresh = MutableLiveData<Boolean>()
    val openController = NavigationParamLiveData<Long>()
    
    private val getDeviceUseCase: GetDeviceUseCase by inject()
    private val getControllersUseCase: GetControllerUseCase by inject()
    private val observeControllerUseCase: ObserveControllerUseCase by inject()
    private val readControllerUseCase: ReadControllerUseCase by inject()
    private val observedControllers = mutableMapOf<Long, Disposable>()
    private val controllers = mutableMapOf<Long, DataStatus<Controller>>()
    private var deviceId: Long = 0
    
    fun setDeviceId(deviceId: Long) {
        this.deviceId = deviceId
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        onRefresh()
    }
    
    fun onRefresh() {
        viewModelScope.launch {
            refresh.postValue(true)
    
            getDeviceUseCase.runCatching { execute(deviceId) }.onSuccess {
                device.value = it
                startObservingControllers(it)
            }
    
            refresh.postValue(false)
        }
    }
    
    private fun startObservingControllers(device: Device) {
        device.controllers.map { id ->
            if (observedControllers.containsKey(id)) return@map
            
            observedControllers[id] = observeControllerUseCase.execute(id)
                .doOnNext {
                    if (it is EmptyStatus) {
                        getControllersUseCase.runInScope(viewModelScope) {
                            execute(id)
                        }
                    }
                }
                .subscribe {
                    controllers[id] = it
                    postControllers()
                }
        }
    }
    
    private fun postControllers() {
        val device = this.device.value ?: return
    
        controllersLiveData.postValue(device.controllers.mapNotNull { controllers[it] })
    }
    
    fun onControllerClick(controllerId: Long) {
        openController.trigger(controllerId)
    }
    
    fun deviceNameChanged(name: String) {
        TODO()
    }
    
    fun deviceDescriptionChanged(description: String) {
        TODO()
    }
    
    override fun onCleared() {
        super.onCleared()
        observedControllers.values.forEach { it.dispose() }
    }
    
    fun onControllerLongClick(id: Long) {
        readControllerUseCase.runInScopeCatchingAny(viewModelScope) { execute(id) }
    }
}