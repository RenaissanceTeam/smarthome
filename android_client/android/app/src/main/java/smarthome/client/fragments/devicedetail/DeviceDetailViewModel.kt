package smarthome.client.fragments.devicedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import smarthome.client.Model
import smarthome.library.common.IotDevice

class DeviceDetailViewModel : ViewModel() {
    private val _device = MutableLiveData<IotDevice>()
    val device: LiveData<IotDevice>
        get() = _device

    private val _refresh = MutableLiveData<Boolean>()
    val refresh: LiveData<Boolean>
        get() = _refresh


    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun setDeviceGuid(deviceGuid: Long?) {
        deviceGuid ?: return

        uiScope.launch {
            _refresh.value = true
            _device.value = Model.getDevice(deviceGuid)
        }
    }

    fun deviceSet() {
        _refresh.value = false
    }
}