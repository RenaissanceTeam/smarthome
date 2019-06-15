package smarthome.client.presentation.fragments.controllerdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.BuildConfig
import smarthome.client.util.HomeModelException
import smarthome.client.domain.usecases.ControllersUseCase
import smarthome.client.domain.usecases.DevicesUseCase
import smarthome.client.domain.usecases.PendingControllersUseCase
import smarthome.client.domain.usecases.PendingDevicesUseCase
import smarthome.client.presentation.fragments.controllerdetail.statechanger.ControllerTypeAdapter
import smarthome.client.presentation.fragments.controllerdetail.statechanger.StateChangerType
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class ControllerDetailViewModel : ViewModel(), KoinComponent {
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

    private var disposable: Disposable? = null

    private var usePending = false
    private val controllersUseCase: ControllersUseCase by inject()
    private val devicesUseCase: DevicesUseCase by inject()
    private val pendingControllersUseCase: PendingControllersUseCase by inject()
    private val pendingDevicesUseCase: PendingDevicesUseCase by inject()

    fun setControllerGuid(controllerGuid: Long?) {
        controllerGuid ?: return
        viewModelScope.launch {
            _refresh.value = true
            try {
                val foundController = if (!usePending) controllersUseCase.getController(controllerGuid) else pendingControllersUseCase.getPendingController(controllerGuid)
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

    fun usePending() {
        usePending = true
    }

    private suspend fun listenForModelChanges(controllerGuid: Long) {
        val observable = if (!usePending) devicesUseCase.getDevices() else pendingDevicesUseCase.getPendingDevices()
        disposable = observable.subscribe {
            val changedController = controllersUseCase.findController(it, controllerGuid)
            _controller.value = changedController
            _device.value = devicesUseCase.findDevice(it, changedController)
            if (changedController.isUpToDate) _refresh.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun newStateRequest(state: String?, serveState: String) {
        if (BuildConfig.DEBUG) Log.d(TAG, "new state request $state")
        val device = _device.value ?: return
        val controller = _controller.value ?: return

        viewModelScope.launch {
            _refresh.value = true
            state?.let { controller.state = it }
            controller.serveState = serveState

            _updateDevice(device)
        }
    }
    fun controllerNameChanged(name: String) {
        val controller = _controller.value ?: return
        val device = _device.value ?: return
        device.controllers.find { it == controller }?.name = name

        updateDevice(device)
    }

    private fun updateDevice(device: IotDevice) {
        viewModelScope.launch {
            _refresh.value = true
            _updateDevice(device)
        }
    }

    private suspend fun _updateDevice(device: IotDevice) {
        if (!usePending)
            devicesUseCase.changeDevice(device)
        else pendingDevicesUseCase.changePendingDevice(device)
    }

}