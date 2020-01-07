package smarthome.client.presentation.devices.devicedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import smarthome.client.domain.api.entity.Device
import smarthome.client.domain.api.usecase.DevicesUseCase


class DeviceDetailViewModel(
    private val devicesUseCase: DevicesUseCase
) : ViewModel(), KoinComponent {
    private val _device = MutableLiveData<Device>()
    val device: LiveData<Device>
        get() = _device

    private val _refresh = MutableLiveData<Boolean>()
    val refresh: LiveData<Boolean>
        get() = _refresh

    // todo replace with rx. When item is consumed, no need to send it again (like LiveData does)
    private val _controllerDetails = MutableLiveData<Long?>()
    val controllerDetails: LiveData<Long?>
        get() = _controllerDetails

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var disposable: Disposable? = null

    fun setDeviceGuid(deviceGuid: Long?) {
        deviceGuid ?: return

        uiScope.launch {
            try {
                _refresh.value = true
                _device.value = devicesUseCase.getDevice(deviceGuid)
                _refresh.value = false

                listenForModelChanges(deviceGuid)
            } catch (e: Throwable) {
                TODO()
            }
        }
    }
    
    private suspend fun listenForModelChanges(id: Long) {
        val observable = devicesUseCase.getDevices()
        disposable = observable.subscribe { devices ->
            val changedDevice = devices.find { it.id == id }
            _device.value = changedDevice
            _refresh.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        disposable?.dispose()
    }

    fun onControllerClick(controllerGuid: Long) {
        _controllerDetails.value = controllerGuid
    }

    fun controllerDetailsShowed() {
        _controllerDetails.value = null
    }

    fun deviceNameChanged(name: String) {
        TODO()
    }

    fun deviceDescriptionChanged(description: String) {
        TODO()
    
    }

    private fun updateDevice(device: Device) {
        TODO()
    }
}