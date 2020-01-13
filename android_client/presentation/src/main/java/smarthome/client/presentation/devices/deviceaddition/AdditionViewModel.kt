package smarthome.client.presentation.devices.deviceaddition

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import com.mikepenz.fastadapter.GenericItem
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.devices.usecase.GetPendingDevicesUseCase
import smarthome.client.entity.Controller
import smarthome.client.entity.Device
import smarthome.client.presentation.components.ControllerItem
import smarthome.client.presentation.components.DeviceItem
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.util.log

class AdditionViewModel : KoinViewModel() {
    val devices = MutableLiveData<List<GenericItem>>()
    private val refresh = MutableLiveData<Boolean>()
    private val getPendingDevicesUseCase: GetPendingDevicesUseCase by inject()
    
    
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        onRefresh()
    }
    
    fun onRefresh() {
        viewModelScope.launch {
            refresh.postValue(true)
            
            getPendingDevicesUseCase.runCatching { execute() }
                .onSuccess {
                    devices.postValue(it.flatMap { device ->
                        listOf(DeviceItem(device)) + device.controllers.map(::ControllerItem)
                    })
                }
                .onFailure { log(it) }
            
            refresh.postValue(false)
        }
    }
    fun onControllerChanged(controller: Controller) {
        TODO()
    }
    
    fun acceptDevice(device: Device?) {
        TODO()
    }
    
    fun rejectDevice(device: Device?) {
        TODO()
    }
    
    fun onDeviceClicked(id: Long) {
        TODO()
    }
    
    fun onControllerClicked(id: Long) {
        TODO()
    }
    
    fun onAddDeviceClicked() {
        TODO()
    }
}