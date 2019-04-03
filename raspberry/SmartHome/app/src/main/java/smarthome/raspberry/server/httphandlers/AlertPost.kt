package smarthome.raspberry.server.httphandlers

import android.util.Log

import fi.iki.elonen.NanoHTTPD
import smarthome.library.common.BaseController
import smarthome.raspberry.AlertException
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.model.SmartHomeRepository


class AlertPost : BaseRequestHandler() {
    val TAG = AlertPost::class.java.simpleName

    private var controllerIndex = -1
    private lateinit var value: String
    private lateinit var device: ArduinoDevice
    private lateinit var controller: BaseController

    override fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        return try {
            parseRequest(session)
            controller.state = value
            Log.d(TAG, "alert! new state=" + value + "for controller " + controller)
            SmartHomeRepository.handleAlert(device, controller)
            NanoHTTPD.Response("alert ok")
        } catch (e: Exception) {
            NanoHTTPD.Response("alert exception $e")
        }
    }

    @Throws(NumberFormatException::class)
    private fun parseRequest(session: NanoHTTPD.IHTTPSession) {
        val params = session.parms
        controllerIndex = Integer.parseInt(params["ind"] ?: throw AlertException("no 'ind' param"))
        val ip = session.headers["http-client-ip"] ?: throw AlertException("no 'http-client-ip' param in headers")
        value = params["value"] ?: throw AlertException("no 'value' param")
        device = SmartHomeRepository.getArduinoByIp(ip)
        controller = device.controllers[controllerIndex]
    }
}
