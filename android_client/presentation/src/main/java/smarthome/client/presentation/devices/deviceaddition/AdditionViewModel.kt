package smarthome.client.presentation.devices.deviceaddition

import androidx.lifecycle.*
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.GetControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.ReadControllerUseCase
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.domain.api.devices.usecase.AcceptPendingDeviceUseCase
import smarthome.client.domain.api.devices.usecase.DeclinePendingDeviceUseCase
import smarthome.client.domain.api.devices.usecase.GetPendingDevicesUseCase
import smarthome.client.entity.Controller
import smarthome.client.presentation.devices.deviceaddition.epoxy.PendingControllerItemState
import smarthome.client.presentation.devices.deviceaddition.epoxy.PendingDeviceItemState
import smarthome.client.presentation.runInScope
import smarthome.client.presentation.runInScopeCatchingAny
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationParamLiveData
import smarthome.client.util.*

class AdditionViewModel : KoinViewModel() {
    val deviceStates = MutableLiveData<List<PendingDeviceItemState>>(listOf())
    val showEmpty = Transformations.map(deviceStates) { it.isEmpty() }
    val refresh = MutableLiveData<Boolean>(false)
    val openControllerDetails = NavigationParamLiveData<Long>()
    val openDeviceDetails = NavigationParamLiveData<Long>()
    
    private val expanded = mutableMapOf<Long, Boolean>()
    private val devices = mutableListOf<GeneralDeviceInfo>()
    private val controllers = mutableMapOf<Long, DataStatus<Controller>>()
    private val acceptInProgress = mutableMapOf<Long, Boolean>()
    private val declineInProgress = mutableMapOf<Long, Boolean>()
    private val observedControllers = mutableMapOf<Long, Disposable>()
    
    private val observeController: ObserveControllerUseCase by inject()
    private val getControllerUseCase: GetControllerUseCase by inject()
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
                    observeCurrentDeviceControllers()
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
        val newState = devices.map { device ->
            PendingDeviceItemState(
                device = device,
                isExpanded = isExpanded(device.id),
                acceptInProgress = acceptInProgress[device.id] ?: false,
                declineInProgress = declineInProgress[device.id] ?: false,
                controllers = device.controllers
                    .map {
                        val controller = controllers[it]
                        PendingControllerItemState(
                            id = it,
                            name = controller?.data?.name.orEmpty(),
                            type = controller?.data?.type.orEmpty(),
                            state = controller?.data?.state.orEmpty(),
                            isRefreshing = controller is LoadingStatus
                        )
                    }
            )
        }
        deviceStates.postValue(newState)
    }
    
    private fun isExpanded(deviceId: Long) = expanded[deviceId] ?: false
    
    private fun observeCurrentDeviceControllers() {
        devices
            .flatMap { it.controllers }
            .also(::removeNonRelevantObservations)
            .map { controllerId ->
                if (observedControllers.containsKey(controllerId)) return@map
                
                observedControllers[controllerId] = observeController
                    .execute(controllerId)
                    .doOnNext {
                        log("status $it")
                        if (it is EmptyStatus) getControllerUseCase.runInScope(viewModelScope) {
                            execute(controllerId)
                        }
                    }
                    .filter { it !is EmptyStatus }
                    .subscribe {
                        controllers[controllerId] = it
                        postPendingDevicesStates()
                    }
            }
    }
    
    private fun removeNonRelevantObservations(ids: List<Long>) {
        observedControllers.keys.subtract(ids).forEach {
            observedControllers[it]?.dispose()
            observedControllers.remove(it)
        }
    }
    
    fun acceptDevice(id: Long) {
        viewModelScope.launch {
            acceptInProgress[id] = true
            postPendingDevicesStates()
        
            acceptPendingDeviceUseCase
                .runCatching { execute(id) }
                .onSuccess {
                    devices.remove(devices.find { it.id == id })
                    observeCurrentDeviceControllers()
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
                    observeCurrentDeviceControllers()
                }
    
            declineInProgress[id] = false
            postPendingDevicesStates()
        }
    }
    
    fun onDeviceLongClicked(id: Long) {
        openDeviceDetails.trigger(id)
    }
    
    fun onControllerClicked(id: Long) {
        readControllerUseCase.runInScopeCatchingAny(viewModelScope) { execute(id) } 
    }
    
    fun onControllerLongClicked(id: Long) {
        openControllerDetails.trigger(id)
    }
    
    fun onAddDeviceClicked() {
        TODO()
    }
    
    override fun onCleared() {
        super.onCleared()
        observedControllers.values.forEach { it.dispose() }
    }
}