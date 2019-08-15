package smarthome.client.presentation.fragments.controllerdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.domain.domain.usecases.ControllersUseCase
import smarthome.client.domain.domain.usecases.DevicesUseCase
import smarthome.client.domain.domain.usecases.PendingControllersUseCase
import smarthome.client.domain.domain.usecases.PendingDevicesUseCase
import smarthome.client.presentation.fragments.controllerdetail.statechanger.StateChangerType
import smarthome.client.util.HomeModelException
import smarthome.client.util.NoControllerException
import smarthome.client.util.NoDeviceWithControllerException
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerServeState
import smarthome.library.common.IotDevice

class ControllerDetailViewModel : ViewModel(), KoinComponent {
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
    private val controllersUseCase: smarthome.client.domain.domain.usecases.ControllersUseCase by inject()
    private val devicesUseCase: smarthome.client.domain.domain.usecases.DevicesUseCase by inject()
    private val pendingControllersUseCase: smarthome.client.domain.domain.usecases.PendingControllersUseCase by inject()
    private val pendingDevicesUseCase: smarthome.client.domain.domain.usecases.PendingDevicesUseCase by inject()

    fun setControllerGuid(controllerGuid: Long?) {
        controllerGuid ?: return
        viewModelScope.launch {
            _refresh.value = true
            try {
                val foundController = if (!usePending) controllersUseCase.getController(controllerGuid) else pendingControllersUseCase.getPendingController(controllerGuid)
                _controller.value = foundController
                if (foundController.serveState == ControllerServeState.IDLE) _refresh.value = false
                listenForModelChanges(controllerGuid)
            } catch (e: HomeModelException) {
                // todo handle
            }
        }
    }

    fun usePending() {
        usePending = true
    }

    private suspend fun listenForModelChanges(controllerGuid: Long) {
        val observable = if (!usePending) devicesUseCase.getDevices() else pendingDevicesUseCase.getPendingDevices()
        disposable = observable.subscribe {
            val changedController = findController(it, controllerGuid)
            _controller.value = changedController
            _device.value = findDevice(it, changedController)
            if (changedController.serveState == ControllerServeState.IDLE) _refresh.value = false
        }
    }

    private fun findController(devices: MutableList<IotDevice>, controllerGuid: Long): BaseController {
        for (device in devices) {
            val controllers = device.controllers
            return controllers.find { it.guid == controllerGuid } ?: continue
        }

        throw NoControllerException(controllerGuid)
    }

    private fun findDevice(devices: MutableList<IotDevice>, controller: BaseController): IotDevice {
        for (device in devices) {
            val controllers = device.controllers
            return if (controllers.contains(controller)) device else continue
        }

        throw NoDeviceWithControllerException(controller)
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun newStateRequest(state: String?, serveState: ControllerServeState) {
        val device = _device.value ?: return
        val controller = _controller.value ?: return

        viewModelScope.launch {
            _refresh.value = true
//            state?.let { controller.state = it }
            controller.serveState = serveState

            changeDevice(device)
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
            changeDevice(device)
        }
    }

    private fun changeDevice(device: IotDevice) {
        viewModelScope.launch {
            if (!usePending) devicesUseCase.changeDevice(device)
            else pendingDevicesUseCase.changePendingDevice(device)
        }
    }
}