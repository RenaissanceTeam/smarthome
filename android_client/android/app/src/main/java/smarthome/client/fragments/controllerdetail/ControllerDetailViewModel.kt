package smarthome.client.fragments.controllerdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import smarthome.client.BuildConfig
import smarthome.client.HomeModelException
import smarthome.client.Model
import smarthome.client.fragments.controllerdetail.statechanger.ControllerTypeAdapter
import smarthome.client.fragments.controllerdetail.statechanger.StateChangerType
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerType
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

    private val _stateChangerType = MutableLiveData<StateChangerType>()
    val stateChangerType: LiveData<StateChangerType>
        get() = _stateChangerType

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var disposable: Disposable? = null

    fun setControllerGuid(controllerGuid: Long?) {
        controllerGuid ?: return
        uiScope.launch {
            _refresh.value = true
            try {
                val foundController = Model.getController(controllerGuid)
                _controller.value = foundController
                if (foundController.isUpToDate) _refresh.value = false
                _stateChangerType.value = ControllerTypeAdapter.toStateChangerType(foundController.type)
                listenForModelChanges(controllerGuid)
            } catch (e: HomeModelException) {
                // todo handle
                if (BuildConfig.DEBUG) Log.w(TAG, "exception when setting controller guid=$controllerGuid", e)
            }
        }
    }

    private suspend fun listenForModelChanges(controllerGuid: Long) {
        disposable = Model.getDevicesObservable().subscribe {
            val changedController = Model.getController(it, controllerGuid)
            _controller.value = changedController
            _device.value = Model.getDevice(it, changedController)
            if (changedController.isUpToDate) _refresh.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        disposable?.dispose()
    }

    fun newStateRequest(state: String?, serveState: String) {
        if (BuildConfig.DEBUG) Log.d(TAG, "new state request $state")
        val device = _device.value ?: return
        val controller = _controller.value ?: return

        uiScope.launch {
            _refresh.value = true
            state?.let { controller.state = it }
            controller.serveState = serveState

            Model.changeDevice(device)
        }
    }

    fun disableController() {
        if (BuildConfig.DEBUG) Log.d(TAG, "disabling controller...")
        val device = _device.value ?: return
        val controller = _controller.value ?: return

        uiScope.launch {
            controller.disableController()

            updateDevice(device)
        }
    }

    fun controllerNameChanged(name: String) {
        val controller = _controller.value ?: return
        val device = _device.value ?: return
        device.controllers.find { it == controller }?.name = name

        updateDevice(device)
    }

    private fun updateDevice(device: IotDevice) {
        uiScope.launch {
            _refresh.value = true
            Model.changeDevice(device)
        }
    }

}