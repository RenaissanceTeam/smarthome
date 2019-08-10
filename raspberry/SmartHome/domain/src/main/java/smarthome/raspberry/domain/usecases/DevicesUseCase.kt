package smarthome.raspberry.domain.usecases

import smarthome.library.common.*
import smarthome.raspberry.domain.HomeRepository

class DevicesUseCase(private val repository: HomeRepository) {

    /**
     * Triggered when new device should be added to the smarthome.
     *
     * New device is considered to be pending until user explicitly accepts it.
     */
    suspend fun addNewDevice(device: IotDevice) {
        val devices = repository.getCurrentDevices()

        if (devices.contains(device)) {
            TODO()
        }

        repository.addPendingDevice(device)
    }

    /**
     * Accepting device means making it a part of the smarthome, after which it should
     * work in it's normal state: user should be able to make read/write requests, create
     * scripts with the device, etc..
     */
    suspend fun acceptPendingDevice(device: IotDevice) {
        repository.removePendingDevice(device)
        repository.addDevice(device)
    }

    suspend fun removeDevice(device: IotDevice) {
        repository.removeDevice(device)
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

            repository.saveDevice(changedDevice)
        }
    }

    private suspend fun handleDeviceChanges(changedDevice: IotDevice): Boolean {
        when (changedDevice.serveState) {
            DeviceServeState.PENDING_TO_ADD -> addNewDevice(changedDevice)
            DeviceServeState.ACCEPT_PENDING_TO_ADD -> acceptPendingDevice(changedDevice)
            DeviceServeState.DELETE -> removeDevice(changedDevice)
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
                        ControllerServeState.PENDING_READ -> readController(it)
                        ControllerServeState.PENDING_WRITE -> {
                            if (it.state == null) {
                                it.serveState = ControllerServeState.IDLE
                            }

                            it.state?.let { writeState ->
                                setControllerState(it, writeState)
                            }
                        }
                        else -> return@map
                    }
                }
                .isNotEmpty()
    }


    private suspend fun readController(controller: BaseController) {
        repository.proceedReadController(controller)
        controller.serveState = ControllerServeState.IDLE
    }

    private suspend fun setControllerState(controller: BaseController, state: ControllerState) {
        repository.proceedWriteController(controller, state)
        controller.serveState = ControllerServeState.IDLE
    }

    private fun hasControllerChanges(notChangedController: BaseController,
                             changedController: BaseController): Boolean {
        return notChangedController.serveState != changedController.serveState
    }
}

