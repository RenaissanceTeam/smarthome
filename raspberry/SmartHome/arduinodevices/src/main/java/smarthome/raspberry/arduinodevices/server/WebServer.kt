package smarthome.raspberry.arduinodevices.server

import android.util.Log
import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.runBlocking
import smarthome.library.common.BaseController
import smarthome.library.common.DeviceChannelOutput
import smarthome.library.common.IotDevice
import smarthome.raspberry.arduinodevices.ArduinoControllerResponse
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.controllers.ArduinoController
import smarthome.raspberry.arduinodevices.server.httphandlers.AlertPost
import smarthome.raspberry.arduinodevices.server.httphandlers.RequestHandler
import java.io.IOException

const val TAG = "WebServer"
const val PORT = 8080

internal interface WebServerOutput {
    fun onAlert(device: ArduinoDevice, controller: ArduinoController)
    fun onNewDevice(device: ArduinoDevice)
}

class WebHandler(private val output: DeviceChannelOutput) {

    suspend fun handle(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        val method = session.method
        val uri = session.uri

        return findSuitableHandler(method, uri).serve(session)
    }


    private fun findSuitableHandler(method: NanoHTTPD.Method, uri: String): RequestHandler {
//        for (handlerType in ) {
//            if (handlerType.method == method && uri.startsWith(handlerType.requestPath)) {
//                return handlerType.handler
//            }
//        }
        return AlertPost(output)

    }

}

internal class WebServer(deviceChannelOutput: DeviceChannelOutput) : NanoHTTPD(PORT), StoppableServer {
    private val handler = WebHandler(deviceChannelOutput)
    override fun serve(session: IHTTPSession): Response {

        return try {
            runBlocking { handler.handle(session) }
        } catch (e: Exception) {
            Log.d(TAG, "can't serve: $e")
            Response("error $e")
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
