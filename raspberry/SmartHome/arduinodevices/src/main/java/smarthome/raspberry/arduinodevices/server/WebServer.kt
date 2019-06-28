package smarthome.raspberry.arduinodevices.server

import android.util.Log
import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.runBlocking
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.raspberry.arduinodevices.ArduinoControllerResponse
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.controllers.ArduinoController
import java.io.IOException

const val TAG = "WebServer"
const val PORT = 8080

internal interface WebServerOutput {
    fun onAlert(device: ArduinoDevice, controller: ArduinoController)
    fun onNewDevice(device: ArduinoDevice)
}

interface DeviceChannelInput {
    fun findController(guid: Long): BaseController
    fun findDevice(controller: BaseController): IotDevice
}


internal class WebServer : NanoHTTPD(PORT), StoppableServer {
    override fun serve(session: IHTTPSession): Response {
        return try {
            runBlocking { HandlerType.handle(session) }
        } catch (e: Exception) {
            Log.d(TAG, "can't serve: $e")
            HandlerType.errorHandle("No suitable method found")
        }
    }

    override fun startServer() {
        try {
            start()
        } catch (e: IOException) {
            Log.e(TAG, "startServer: ", e)
        }
    }

    override fun stopServer() {
        stop()
    }
}
