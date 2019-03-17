package smarthome.raspberry.server.httphandlers

import android.util.Log

import fi.iki.elonen.NanoHTTPD
import smarthome.library.common.BaseController
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.model.SmartHomeRepository

val TAG = AlertPost::class.java.simpleName
class AlertPost : BaseRequestHandler() {

    override fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        return try {
            serveAlertRequest(session)
            NanoHTTPD.Response("alert ok")
        } catch (e: Exception) {
            NanoHTTPD.Response("alert exception $e")
        }

    }

    @Throws(NumberFormatException::class)
    private fun serveAlertRequest(session: NanoHTTPD.IHTTPSession) {
        val params = session.parms
        val index = Integer.parseInt(params["ind"]!!)
        val value = params["value"]
        val ip = session.headers["http-client-ip"] ?: return

        val arduino = SmartHomeRepository.getArduinoByIp(ip)
        val controller = arduino.controllers[index]
        controller.state = value
        Log.d(TAG, "alert! new state=" + value + "for controller " + controller)
        // todo save to firestore (notify android client, send FCM)
    }
}
