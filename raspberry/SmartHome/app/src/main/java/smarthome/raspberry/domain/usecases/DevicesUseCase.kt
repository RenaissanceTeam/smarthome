package smarthome.raspberry.domain.usecases

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.raspberry.domain.HomeRepository

class DevicesUseCase(private val repository: HomeRepository) {
    suspend fun addNewDevice(device: IotDevice) {}

    suspend fun removeDevice(device: IotDevice) {

    }

    suspend fun onDevicesChanged(changedDevices: MutableList<IotDevice>) {
        try {
            handleChanges(changedDevices, repository.getCurrentDevices())
        } catch (e: Throwable) {
        }
    }

    private suspend fun handleChanges(changedDevices: List<IotDevice>, notChangedDevices: MutableList<IotDevice>) {
        for (changedDevice in changedDevices) {
            val notChangedDevice = notChangedDevices.find { it == changedDevice } ?: continue

            val hasChanges = handleDeviceChanges(changedDevice, notChangedDevice)

            if (hasChanges) {
                TODO()
            }
        }
    }

    private suspend fun handleDeviceChanges(changedDevice: IotDevice,
                                            notChangedDevice: IotDevice): Boolean {

        return changedDevice.controllers
                .filter { changedController ->
                    val notChangedController = notChangedDevice.controllers
                            .find { it == changedController } ?: return@filter false

                    hasControllerChanges(notChangedController, changedController)
                }
                // todo parse pending read or pending write and call repo
                .map { repository.proceedControllerChange(it) }
                .isNotEmpty()
    }


    private fun hasControllerChanges(notChangedController: BaseController,
                             changedController: BaseController): Boolean {
        return notChangedController.state != changedController.state
    }


}

