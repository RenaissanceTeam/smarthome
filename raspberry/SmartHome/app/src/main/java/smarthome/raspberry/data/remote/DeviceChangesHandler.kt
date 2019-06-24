package smarthome.raspberry.data.remote

import android.util.Log
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.raspberry.OddControllerInCloud
import smarthome.raspberry.model.SmartHomeRepository

class DeviceChangesHandler(private val localDevice: IotDevice,
                           private val cloudDevice: IotDevice) {

    var changesMade = false

    suspend fun handleChanges() {

        var shouldUpdateDb = false

        for (cloudController in cloudDevice.controllers) {
            try {
                val changesHandler = handleControllerChanges(getLocalController(cloudController), cloudController)
                shouldUpdateDb = shouldUpdateDb || changesHandler.shouldUpdateDb
            } catch (e: Throwable) {
                Log.d("DeviceChangesHandler", "", e)
            }
        }

        if (!localDevice.isIdentical(cloudDevice) || shouldUpdateDb) {
            localDevice.name = cloudDevice.name
            localDevice.description = cloudDevice.description
            localDevice.status = cloudDevice.status

            SmartHomeRepository.addDevice(localDevice) // todo test, it should update the device in db
        }
    }

    private fun getLocalController(controller: BaseController): BaseController {
        return localDevice.controllers.find { it == controller }
                ?: throw OddControllerInCloud(controller)
    }

    private suspend fun handleControllerChanges(localController: BaseController,
                                                controller: BaseController): ControllerChangesHandler {
        val changesHandler = ControllerChangesHandler(localController, controller)

        changesHandler.handleChanges()
        if (changesHandler.changesMade) {
            changesMade = true
        }

        return changesHandler
    }
}