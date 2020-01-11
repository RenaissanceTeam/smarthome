package smarthome.client.presentation.devices.devicedetail

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.devices.usecase.GetDeviceUseCase
import smarthome.client.domain.api.usecase.DevicesUseCase
import smarthome.client.entity.Device
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationParamLiveData


class DeviceDetailViewModel : KoinViewModel(), LifecycleObserver {
    val device = MutableLiveData<Device>()
    val refresh = MutableLiveData<Boolean>()
    val openController = NavigationParamLiveData<Long>()
    private val getDeviceUseCase: GetDeviceUseCase by inject()
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
            getDeviceUseCase.runCatching { execute(deviceId) }.onSuccess { device.postValue(it) }
            refresh.postValue(false)
        }
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
}