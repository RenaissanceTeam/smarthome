package smarthome.client.presentation.devices.controllerdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.domain.usecases.ControllersUseCase
import smarthome.client.domain.usecases.DevicesUseCase
import smarthome.client.domain.usecases.PendingControllersUseCase
import smarthome.client.domain.usecases.PendingDevicesUseCase
import smarthome.client.entity.HomeException
import smarthome.client.presentation.NoControllerException
import smarthome.client.presentation.NoDeviceWithControllerException
import smarthome.client.presentation.devices.controllerdetail.statechanger.StateChangerType




class ControllerDetailViewModel : ViewModel(), KoinComponent {
    private val _refresh = MutableLiveData<Boolean>()
    val refresh: LiveData<Boolean>
        get() = _refresh

    private val _controller = MutableLiveData<Controller>()
    val controller: LiveData<Controller>
        get() = _controller

    private val _device = MutableLiveData<Device>()
    val device: LiveData<Device>
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
                if (foundController.serveState == ControllerServeState.IDLE) _refresh.value = false
                listenForModelChanges(controllerGuid)
            } catch (e: HomeException) {
                TODO()
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

    private fun findController(devices: MutableList<Device>, controllerGuid: Long): Controller {
        for (device in devices) {
            val controllers = device.controllers
            return controllers.find { it.guid == controllerGuid } ?: continue
        }

        throw NoControllerException(controllerGuid)
    }

    private fun findDevice(devices: MutableList<Device>, controller: Controller): Device {
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

    private fun updateDevice(device: Device) {
        viewModelScope.launch {
            _refresh.value = true
            changeDevice(device)
        }
    }

    private fun changeDevice(device: Device) {
        viewModelScope.launch {
            if (!usePending) devicesUseCase.changeDevice(device)
            else pendingDevicesUseCase.changePendingDevice(device)
        }
    }
}