package smarthome.raspberry.server.httphandlers

import com.google.gson.Gson

import java.io.IOException

import fi.iki.elonen.NanoHTTPD
import smarthome.library.common.BaseController
import smarthome.raspberry.arduinodevices.ArduinoControllerResponse
import smarthome.raspberry.arduinodevices.controllers.ArduinoReadable

class ControllerGet : BaseRequestHandler() {

    override suspend fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        val params = session.parms
        val controller = getController(params)
        return if (controller is ArduinoReadable) {
            try {
                val response = (controller as ArduinoReadable).read()
                NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, "text/json",
                        Gson().toJson(response))
            } catch (e: IOException) {
                arduinoHttpError
            }

        } else {
            throw IllegalStateException("get request to non readable deviceObserver")
        }
    }
}
