package smarthome.raspberry.input.domain

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerServeState
import smarthome.library.common.DeviceServeState
import smarthome.library.common.IotDevice
import smarthome.raspberry.controllers.api.domain.ReadControllerUseCase
import smarthome.raspberry.controllers.api.domain.WriteControllerUseCase
import smarthome.raspberry.devices.api.domain.*
import smarthome.raspberry.input.api.domain.HandleInputByParsingChangedDevicesUseCase

// todo someone should start this use case. previously was
//fun init() {
//        input.setActionForNewDeviceUpdate {
//            if (it.isInnerCall) return@setActionForNewDeviceUpdate
//            ioScope.execute { onUserRequest(it.devices) }
//        }
//    }

class HandleInputByParsingChangedDevicesUseCaseImpl(
        private val getDevicesUseCase: GetDevicesUseCase,
        private val addDeviceUseCase: AddDeviceUseCase,
        private val acceptPendingDeviceUseCase: AcceptPendingDeviceUseCase,
        private val readControllerUseCase: ReadControllerUseCase,
        private val writeControllerUseCase: WriteControllerUseCase,
        private val removeDeviceUseCase: RemoveDeviceUseCase
) : HandleInputByParsingChangedDevicesUseCase {

    override suspend fun execute(changedDevices: MutableList<IotDevice>) {
        try {
            handleChanges(changedDevices, getDevicesUseCase.execute())
        } catch (e: Throwable) {
            throw e
        }
    }

    // todo test: when change only name should NOT trigger remote update
    private suspend fun handleChanges(changedDevices: List<IotDevice>,
                                      notChangedDevices: List<IotDevice>) {
        for (changedDevice in changedDevices) {
            val notChangedDevice = notChangedDevices.find { it == changedDevice } ?: continue

            handleDeviceChanges(changedDevice)
            handleControllerChanges(changedDevice, notChangedDevice)
        }
    }

    private suspend fun handleDeviceChanges(changedDevice: IotDevice): Boolean {
        when (changedDevice.serveState) {
            DeviceServeState.PENDING_TO_ADD -> addDeviceUseCase.execute(changedDevice)
            DeviceServeState.ACCEPT_PENDING_TO_ADD -> acceptPendingDeviceUseCase.execute(
                    changedDevice)
            DeviceServeState.DELETE -> removeDeviceUseCase.execute(changedDevice)
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
                            readControllerUseCase.execute(changedDevice, it)
                        ControllerServeState.PENDING_WRITE -> {
                            if (it.state == null) {
                                it.serveState = ControllerServeState.IDLE
                            }

                            it.state?.let { writeState ->
                                writeControllerUseCase.execute(changedDevice, it, writeState)
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