package smarthome.raspberry.model.cloudchanges

import smarthome.library.common.BaseController
import smarthome.raspberry.UnsupportedRead
import smarthome.raspberry.UnsupportedWrite
import smarthome.raspberry.arduinodevices.controllers.ArduinoReadable
import smarthome.raspberry.arduinodevices.controllers.ArduinoWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightWritable
import java.lang.Exception

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
        changesMade = when (localController) {
            is ArduinoReadable -> {
                val response = localController.read().response // blocking
                localController.state = response
                localController.setUpToDate()
                true
            }
            is YeelightReadable -> {
                localController.setNewState(localController.read()) // blocking
                localController.setUpToDate()
                true
            }
            is GatewayReadable -> {
                localController.setNewState(localController.read()) // non-blocking
                localController.setUpToDate()
                true
            }
            else -> throw UnsupportedRead(localController)
        }
    }

    private suspend fun handleWrite() {
        var newState = cloudController.state
        changesMade = when (localController) {
            is ArduinoWritable -> {
                localController.write(newState) // blocking
                localController.setUpToDate()
                true
            }
            is YeelightWritable -> {
                if(newState == null)
                    newState = ""
                try {
                    val res = localController.write(newState) // blocking
                    if (res.isOkResult())
                        localController.setNewState(newState)
                } catch (e: Exception) { }
                localController.setUpToDate()
                true
            }
            is GatewayWritable -> {
                if (newState == null)
                    newState = ""
                localController.write(newState) // async
                true
            }
            else -> throw UnsupportedWrite(localController, newState)
        }
    }
}