package smarthome.client.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.FetchControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.ReadControllerUseCase
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.domain.api.devices.usecase.GetGeneralDevicesInfo
import smarthome.client.entity.Controller
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationParamLiveData
import smarthome.client.util.DataStatus
import smarthome.client.util.EmptyStatus
import smarthome.client.util.runInScope
import smarthome.client.util.runInScopeCatchingAny


class DashboardViewModel : KoinViewModel() {
    private val getDevicesUseCase: GetGeneralDevicesInfo by inject()
    private val observeControllerUseCase: ObserveControllerUseCase by inject()
    private val fetchControllerUseCase: FetchControllerUseCase by inject()
    private val readControllerUseCase: ReadControllerUseCase by inject()
    
    val items = MutableLiveData<List<GeneralDeviceInfo>>()
    val allHomeUpdateState = MutableLiveData<Boolean>()
    val devices = mutableListOf<GeneralDeviceInfo>()
    val controllers = mutableMapOf<Long, DataStatus<Controller>>()
    val openControllerDetails = NavigationParamLiveData<Long>()
    val openDeviceDetails = NavigationParamLiveData<Long>()
    private val controllersObserving = mutableMapOf<Long, Disposable>()
    
    
    override fun onResume() {
        onRefresh()
    }
    
    fun onRefresh() {
        viewModelScope.launch {
            allHomeUpdateState.value = true
    
            getDevicesUseCase.runCatching { execute() }.onSuccess { activeDevices ->
                devices.apply {
                    clear()
                    addAll(activeDevices)
                    postItems()
                }
    
                observeControllersOf(activeDevices)
            }

            allHomeUpdateState.value = false
        }
    }
    
    private fun observeControllersOf(devices: List<GeneralDeviceInfo>) {
        devices
            .flatMap { it.controllers }
            .map { controllerId ->
                if (controllersObserving.containsKey(controllerId)) return@map
            
                controllersObserving[controllerId] = observeControllerUseCase
                    .execute(controllerId)
                    .doOnNext {
                        if (it is EmptyStatus) fetchControllerUseCase.runInScope(viewModelScope) {
                            execute(controllerId)
                        }
                    }
                    .subscribe {
                        controllers[controllerId] = it
                        postItems()
                    }
            }
    }
    
    override fun onCleared() {
        super.onCleared()
        controllersObserving.values.forEach { it.dispose() }
    }
    
    private fun postItems() {
        items.postValue(devices)
    }
    
    fun onDeviceClicked(id: Long) {
        openDeviceDetails.trigger(id)
    }
    
    fun onControllerClick(id: Long) {
        openControllerDetails.trigger(id)
    }
    
    fun onControllerLongClick(id: Long) {
        readControllerUseCase.runInScopeCatchingAny(viewModelScope) { execute(id) }
    }
}