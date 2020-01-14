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
    private val acceptInProgress = mutableMapOf<Long, Boolean>()
    private val declineInProgress = mutableMapOf<Long, Boolean>()
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
            devices.map {
                PendingDeviceItemState(
                    device = it,
                    isExpanded = isExpanded(it.id),
                    controllerRefreshing = refreshingControllers,
                    acceptInProgress = acceptInProgress[it.id] ?: false,
                    declineInProgress = declineInProgress[it.id] ?: false
                )
            }
        )
    }
    
    private fun isExpanded(deviceId: Long) = expanded[deviceId] ?: false
    
    fun acceptDevice(id: Long) {
        viewModelScope.launch {
            acceptInProgress[id] = true
            postPendingDevicesStates()
        
            acceptPendingDeviceUseCase
                .runCatching { execute(id) }
                .onSuccess {
                    devices.remove(devices.find { it.id == id })
                }
        
            acceptInProgress[id] = false
            postPendingDevicesStates()
        }
    
    }
    
    fun declineDevice(id: Long) {
        viewModelScope.launch {
            declineInProgress[id] = true
            postPendingDevicesStates()
        
            declinePendingDeviceUseCase
                .runCatching { execute(id) }
                .onSuccess {
                    devices.remove(devices.find { it.id == id })
                }
    
            declineInProgress[id] = false
            postPendingDevicesStates()
        }
    }
    
    fun onDeviceLongClicked(id: Long) {
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