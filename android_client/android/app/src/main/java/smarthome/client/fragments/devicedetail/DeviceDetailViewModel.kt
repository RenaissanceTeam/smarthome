package smarthome.client.fragments.devicedetail

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import smarthome.client.HomeModelException
import smarthome.client.Model
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class DeviceDetailViewModel : ViewModel() {
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

    fun setDeviceGuid(deviceGuid: Long?) {
        deviceGuid ?: return

        uiScope.launch {
            try {
                _refresh.value = true
                _device.value = Model.getDevice(deviceGuid)
                _refresh.value = false

                listenForModelChanges(deviceGuid)
            } catch (e: HomeModelException) {

            }
        }
    }

    private suspend fun listenForModelChanges(deviceGuid: Long) {
        disposable = Model.getDevicesObservable().subscribe {
            val changedDevice = Model.getDevice(it, deviceGuid)
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
        uiScope.launch {
            _refresh.value = true
            device.name = name
            Model.changeDevice(device)
        }

    }
}