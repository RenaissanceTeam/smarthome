package smarthome.client.presentation.devices.deviceaddition

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.ReadControllerUseCase
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.domain.api.devices.usecase.AcceptPendingDeviceUseCase
import smarthome.client.domain.api.devices.usecase.DeclinePendingDeviceUseCase
import smarthome.client.domain.api.devices.usecase.GetPendingDevicesUseCase
import smarthome.client.presentation.devices.deviceaddition.epoxy.PendingDeviceItemState
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationParamLiveData
import smarthome.client.util.log

class AdditionViewModel : KoinViewModel() {
    val deviceStates = MutableLiveData<List<PendingDeviceItemState>>(listOf())
    val showEmpty = Transformations.map(deviceStates) { it.isEmpty() }
    val refresh = MutableLiveData<Boolean>(false)
    val openControllerDetails = NavigationParamLiveData<Long>()
    val openDeviceDetails = NavigationParamLiveData<Long>()
    
    private val expanded = mutableMapOf<Long, Boolean>()
    private val devices = mutableListOf<GeneralDeviceInfo>()
    private val refreshingControllers = mutableMapOf<Long, Boolean>()
    private val readControllerUseCase: ReadControllerUseCase by inject()
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
                    devices.clear()
                    devices.addAll(it)
                    postPendingDevicesStates()
                }
                .onFailure { log(it) }
            
            refresh.postValue(false)
        }
    }
    
    fun onExpand(deviceId: Long) {
        expanded[deviceId] = isExpanded(deviceId).not()
        postPendingDevicesStates()
    }
    
    private fun postPendingDevicesStates() {
        deviceStates.postValue(
            devices.map { PendingDeviceItemState(it, isExpanded(it.id), refreshingControllers) }
        )
    }
    
    private fun isExpanded(deviceId: Long) = expanded[deviceId] ?: false
    
    suspend fun acceptDevice(id: Long) {
        acceptPendingDeviceUseCase.execute(id)
        postWithRemovedDevice(id)
    }
    
    private fun postWithRemovedDevice(id: Long) {
//        devices.value?.let {
//            devices.postValue(it.filter { it.id != id })
//        }
    }
    
    suspend fun declineDevice(id: Long) {
        declinePendingDeviceUseCase.execute(id)
        postWithRemovedDevice(id)
    }
    
    fun onDeviceClicked(id: Long) {
        openDeviceDetails.trigger(id)
    }
    
    fun onControllerClicked(id: Long) {
        viewModelScope.launch { readControllerUseCase.runCatching { execute(id) } }
    }
    
    fun onControllerLongClicked(id: Long) {
        openControllerDetails.trigger(id)
    }
    
    fun onAddDeviceClicked() {
        TODO()
    }
}