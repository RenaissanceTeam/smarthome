package smarthome.raspberry.arduinodevices.server.httphandlers

import android.util.Log

import fi.iki.elonen.NanoHTTPD
import smarthome.library.common.ControllerState
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.controllers.ArduinoController


internal class AlertPost : BaseRequestHandler() {
    val TAG = AlertPost::class.java.simpleName

    private var controllerIndex = -1
    private lateinit var value: ControllerState
    private lateinit var device: ArduinoDevice
    private lateinit var controller: ArduinoController

    override suspend fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        return try {
            parseRequest(session)
            controller.state = value
            Log.d(TAG, "alert! new state=" + value + "for controller " + controller)
            output.onAlert(device, controller)
            NanoHTTPD.Response("alert ok")
        } catch (e: Exception) {
            NanoHTTPD.Response("alert exception $e")
        }
    }

    private fun parseRequest(session: NanoHTTPD.IHTTPSession) {
//        val params = session.parms
//        controllerIndex = Integer.parseInt(params["ind"] ?: TODO())
//        val ip = session.headers["http-client-ip"] ?: TODO()
//        value = params["value"] ?: TODO()
//        device = SmartHomeRepository.getArduinoByIp(ip)
//        controller = device.controllers[controllerIndex]

        TODO()
    }
}
