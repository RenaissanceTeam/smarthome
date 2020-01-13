package smarthome.client.presentation.devices.deviceaddition

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import com.mikepenz.fastadapter.GenericItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.devices.usecase.AcceptPendingDeviceUseCase
import smarthome.client.domain.api.devices.usecase.DeclinePendingDeviceUseCase
import smarthome.client.domain.api.devices.usecase.GetPendingDevicesUseCase
import smarthome.client.entity.Controller
import smarthome.client.entity.Device
import smarthome.client.presentation.components.DeviceItem
import smarthome.client.presentation.devices.deviceaddition.items.PendingDevice
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationParamLiveData
import smarthome.client.util.log

class AdditionViewModel : KoinViewModel() {
    val devices = MutableLiveData<List<PendingDevice>>()
    val refresh = MutableLiveData<Boolean>()
    val openControllerDetails = NavigationParamLiveData<Long>()
    val openDeviceDetails = NavigationParamLiveData<Long>()
    private val getPendingDevicesUseCase: GetPendingDevicesUseCase by inject()
    private val acceptPendingDeviceUseCase: AcceptPendingDeviceUseCase by inject()
    private val declinePendingDeviceUseCase: DeclinePendingDeviceUseCase by inject()
    
    
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        onRefresh()
    }
    
    fun onRefresh() {
        viewModelScope.launch {
            refresh.postValue(true)
            
            getPendingDevicesUseCase.runCatching { execute() }
                .onSuccess {
                    devices.postValue(it.map { PendingDevice(it, this@AdditionViewModel) })
                }
                .onFailure { log(it) }
            
            refresh.postValue(false)
        }
    }
    
    suspend fun acceptDevice(id: Long) {
        acceptPendingDeviceUseCase.execute(id)
        postWithRemovedDevice(id)
    }
    
    private fun postWithRemovedDevice(id: Long) {
        devices.value?.let {
            devices.postValue(it.filter { it.device.id != id })
        }
    }
    
    suspend fun declineDevice(id: Long) {
        declinePendingDeviceUseCase.execute(id)
        postWithRemovedDevice(id)
    }
    
    fun onDeviceClicked(id: Long) {
        openDeviceDetails.trigger(id)
    }
    
    fun onControllerClicked(id: Long) {
        openControllerDetails.trigger(id)
    }
    
    fun onControllerLongClicked(id: Long) {
        // open details
    }
    
    fun onAddDeviceClicked() {
        TODO()
    }
}