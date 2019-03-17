package smarthome.raspberry.model

import smarthome.library.common.BaseController
import smarthome.raspberry.UnsupportedRead
import smarthome.raspberry.UnsupportedWrite
import smarthome.raspberry.arduinodevices.controllers.ArduinoReadable
import smarthome.raspberry.arduinodevices.controllers.ArduinoWritable

class ControllerChangesHandler(private val localController: BaseController,
                               private val cloudController: BaseController) {
    var changesMade = false
    var shouldUpdateDb = false

    suspend fun handleChanges() {
        if (!localController.isIdentical(cloudController)) {
            localController.name = cloudController.name // todo update this info in db
            shouldUpdateDb = true
        }

        if (localController.serveState == cloudController.serveState) return

        if (cloudController.isPendingRead) {
            handleRead()
        } else if (cloudController.isPendingWrite) {
            handleWrite()
        }
    }

    private suspend fun handleRead() {
        when (localController) {
            is ArduinoReadable -> {
                val response = localController.read().response // blocking
                localController.state = response
                localController.setUpToDate()
                changesMade = true
            }
            // todo add others
            else -> throw UnsupportedRead(localController)
        }
    }

    private suspend fun handleWrite() {
        val newState = cloudController.state
        when (localController) {
            is ArduinoWritable -> {
                localController.write(newState) // blocking
                localController.setUpToDate()
                changesMade = true
            }
            else -> throw UnsupportedWrite(localController, newState)
        }
    }
}