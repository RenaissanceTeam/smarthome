package smarthome.raspberry.domain.usecases

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerServeState
import smarthome.library.common.ControllerState
import smarthome.library.common.IotDevice
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


    suspend fun readController(device: IotDevice, controller: BaseController) {
        repository.proceedReadController(controller)
        controller.serveState = ControllerServeState.IDLE

        repository.saveDevice(device)
    }

    suspend fun writeController(device: IotDevice, controller: BaseController, state: ControllerState) {
        repository.proceedWriteController(controller, state)
        controller.serveState = ControllerServeState.IDLE

        repository.saveDevice(device)
    }
}

