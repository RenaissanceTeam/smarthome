package smarthome.raspberry.domain.usecases

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.ServeState
import smarthome.raspberry.domain.HomeRepository

class DevicesUseCase(private val repository: HomeRepository) {
    suspend fun addNewDevice(device: IotDevice) {

    }

    suspend fun removeDevice(device: IotDevice) {

    }

    suspend fun onUserRequest(changedDevices: MutableList<IotDevice>) {
        try {
            handleChanges(changedDevices, repository.getCurrentDevices())
        } catch (e: Throwable) {
            TODO()
        }
    }

    private suspend fun handleChanges(changedDevices: List<IotDevice>, notChangedDevices: MutableList<IotDevice>) {
        for (changedDevice in changedDevices) {
            val notChangedDevice = notChangedDevices.find { it == changedDevice } ?: continue

            val hasChanges = handleDeviceChanges(changedDevice, notChangedDevice)
            if (hasChanges) {
                repository.saveDevice(changedDevice)
            }
        }
    }

    private suspend fun handleDeviceChanges(changedDevice: IotDevice,
                                            notChangedDevice: IotDevice): Boolean {
        return changedDevice.controllers
                .filter { changedController ->
                    val notChangedController = notChangedDevice.controllers
                            .find { it == changedController } ?: return@filter false

                    return@filter hasControllerChanges(notChangedController, changedController)
                }
                .map {
                    when (it.serveState) {
                        ServeState.PENDING_READ -> {
                            repository.proceedReadController(it)
                            it.serveState = ServeState.IDLE
                        }
                        ServeState.PENDING_WRITE -> {
                            repository.proceedWriteController(it)
                            it.serveState = ServeState.IDLE
                        }
                        else -> return@map
                    }
                }
                .isNotEmpty()
    }

    private fun hasControllerChanges(notChangedController: BaseController,
                             changedController: BaseController): Boolean {
        return notChangedController.state != changedController.state
    }
}

