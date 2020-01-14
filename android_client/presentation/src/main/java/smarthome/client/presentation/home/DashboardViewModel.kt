package smarthome.client.presentation.home

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import com.mikepenz.fastadapter.GenericItem
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.GetControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.domain.api.devices.usecase.GetGeneralDevicesInfo
import smarthome.client.entity.Controller
import smarthome.client.presentation.components.ControllerItem
import smarthome.client.presentation.components.DeviceItem
import smarthome.client.presentation.runInScope
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.util.Data
import smarthome.client.util.EmptyStatus


class DashboardViewModel : KoinViewModel() {
    private val getDevicesUseCase: GetGeneralDevicesInfo by inject()
    private val observeControllerUseCase: ObserveControllerUseCase by inject()
    private val getControllerUseCase: GetControllerUseCase by inject()
    
    val items = MutableLiveData<List<GenericItem>>()
    val allHomeUpdateState = MutableLiveData<Boolean>()
    val devices = mutableListOf<GeneralDeviceInfo>()
    val controllers = mutableMapOf<Long, Controller>()
    private val controllersObserving = mutableMapOf<Long, Disposable>()
    
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
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
        
                activeDevices
                    .flatMap { device -> device.controllers.map { controller -> controller.id } }
                    .map { controllerId ->
                        if (controllersObserving.containsKey(controllerId)) return@map
                
                        val disposable = observeControllerUseCase.execute(controllerId).subscribe {
                            if (it is Data) {
                                val newController = it.data
                                controllers[newController.id] = newController
                                postItems()
                            }
                            if (it is EmptyStatus) getControllerUseCase.runInScope(viewModelScope) {
                                execute(controllerId)
                            }
                        }
                
                        controllersObserving[controllerId] = disposable
                    }
            }

            allHomeUpdateState.value = false
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        controllersObserving.values.forEach { it.dispose() }
    }
    
    private fun postItems() {
        val items = devices.flatMap { device ->
            listOf(DeviceItem(device)) + device.controllers.map {
                ControllerItem(controllers[it.id])
            }
        }
        this.items.postValue(items)
    }
}