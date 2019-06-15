package smarthome.client.fragments.devicedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.HomeModelException
import smarthome.client.domain.usecases.DevicesUseCase
import smarthome.client.domain.usecases.PendingDevicesUseCase
import smarthome.client.model.Model
import smarthome.library.common.IotDevice

class DeviceDetailViewModel : ViewModel(), KoinComponent {
    private val _device = MutableLiveData<IotDevice>()
    val device: LiveData<IotDevice>
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
    private val devicesUseCase: DevicesUseCase by inject()
    private val pendingDevicesUseCase: PendingDevicesUseCase by inject()

    var usePending: Boolean = false

    fun setDeviceGuid(deviceGuid: Long?) {
        deviceGuid ?: return

        uiScope.launch {
            try {
                _refresh.value = true
                _device.value = if (!usePending) devicesUseCase.getDevice(deviceGuid) else pendingDevicesUseCase.getPendingDevice(deviceGuid)
                _refresh.value = false

                listenForModelChanges(deviceGuid)
            } catch (e: HomeModelException) {

            }
        }
    }

    private suspend fun listenForModelChanges(deviceGuid: Long) {
        val observable = if (!usePending) devicesUseCase.getDevices() else pendingDevicesUseCase.getPendingDevices()
        disposable = observable.subscribe { devices ->
            val changedDevice = devices.find { it.guid == deviceGuid }
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
        val device = _device.value ?: return
        device.name = name
        updateDevice(device)
    }

    fun deviceDescriptionChanged(description: String) {
        val device = _device.value ?: return
        device.description = description
        updateDevice(device)
    }

    fun usePending() {
        usePending = true
    }

    private fun updateDevice(device: IotDevice) {
        uiScope.launch {
            _refresh.value = true
            if (!usePending)
                devicesUseCase.changeDevice(device)
            else pendingDevicesUseCase.changePendingDevice(device)
        }
    }
}