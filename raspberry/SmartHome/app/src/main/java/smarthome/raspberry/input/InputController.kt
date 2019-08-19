package smarthome.raspberry.input

import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerServeState
import smarthome.library.common.DeviceServeState
import smarthome.library.common.IotDevice
import smarthome.raspberry.domain.HomeRepository
import smarthome.raspberry.domain.usecases.DevicesUseCase

class InputController(private val devicesUseCase: DevicesUseCase,
                      private val repository: HomeRepository,
                      private val input: InputControllerDataSource) {
    private val ioScope = CoroutineScope(Dispatchers.IO)

    fun init() {
        ioScope.launch {
            input.onDeviceUpdate {
                if (it.isInnerCall) return@onDeviceUpdate
                ioScope.launch { onUserRequest(it.devices) }
            }
        }
    }

    suspend fun onUserRequest(changedDevices: MutableList<IotDevice>) {
        try {
            handleChanges(changedDevices, repository.getCurrentDevices())
        } catch (e: Throwable) {
            TODO()
        }
    }

    // todo test: when change only name should NOT trigger remote update
    private suspend fun handleChanges(changedDevices: List<IotDevice>,
                                      notChangedDevices: MutableList<IotDevice>) {
        for (changedDevice in changedDevices) {
            val notChangedDevice = notChangedDevices.find { it == changedDevice } ?: continue

            handleDeviceChanges(changedDevice)
            handleControllerChanges(changedDevice, notChangedDevice)
        }
    }

    private suspend fun handleDeviceChanges(changedDevice: IotDevice): Boolean {
        when (changedDevice.serveState) {
            DeviceServeState.PENDING_TO_ADD -> devicesUseCase.addNewDevice(changedDevice)
            DeviceServeState.ACCEPT_PENDING_TO_ADD -> devicesUseCase.acceptPendingDevice(
                    changedDevice)
            DeviceServeState.DELETE -> devicesUseCase.removeDevice(changedDevice)
            else -> return false
        }
        return true
    }

    private suspend fun handleControllerChanges(changedDevice: IotDevice,
                                                notChangedDevice: IotDevice): Boolean {
        return changedDevice.controllers
                .filter { changedController ->
                    val notChangedController = notChangedDevice.controllers
                            .find { it == changedController } ?: return@filter false

                    return@filter hasControllerChanges(notChangedController, changedController)
                }
                .map {
                    when (it.serveState) {
                        ControllerServeState.PENDING_READ ->
                            devicesUseCase.readController(changedDevice, it)
                        ControllerServeState.PENDING_WRITE -> {
                            if (it.state == null) {
                                it.serveState = ControllerServeState.IDLE
                            }

                            it.state?.let { writeState ->
                                devicesUseCase.writeController(changedDevice, it, writeState)
                            }
                        }
                        else -> return@map
                    }
                }
                .isNotEmpty()
    }

    private fun hasControllerChanges(notChangedController: BaseController,
                                     changedController: BaseController): Boolean {
        return notChangedController.serveState != changedController.serveState
    }
}