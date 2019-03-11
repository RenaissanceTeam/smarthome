package smarthome.client.fragments.controllerdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import smarthome.client.BuildConfig
import smarthome.client.HomeModelException
import smarthome.client.Model
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class ControllerDetailViewModel : ViewModel() {
    val TAG = "ControllerDetail_VModel"

    private val _refresh = MutableLiveData<Boolean>()
    val refresh: LiveData<Boolean>
        get() = _refresh

    private val _controller = MutableLiveData<BaseController>()
    val controller: LiveData<BaseController>
        get() = _controller

    private val _device = MutableLiveData<IotDevice>()
    val device: LiveData<IotDevice>
        get() = _device

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun setControllerGuid(controllerGuid: Long?) {
        controllerGuid ?: return
        uiScope.launch {
            _refresh.value = true
            try {
                val foundController = Model.getController(controllerGuid)
                _controller.value = foundController
                _device.value = Model.getDevice(foundController)
            } catch (e: HomeModelException) {
                // todo handle
                if (BuildConfig.DEBUG) Log.w(TAG, "exception when setting controller guid=$controllerGuid", e)

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun controllerSet() {
        _refresh.value = false
    }

}