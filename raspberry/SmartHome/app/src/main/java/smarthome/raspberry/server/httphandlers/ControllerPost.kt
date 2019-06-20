package smarthome.raspberry.server.httphandlers

import android.util.Log

import com.google.gson.Gson

import java.io.IOException

import fi.iki.elonen.NanoHTTPD
import smarthome.library.common.BaseController
import smarthome.raspberry.arduinodevices.ArduinoControllerResponse
import smarthome.raspberry.arduinodevices.controllers.ArduinoWritable

class ControllerPost : BaseRequestHandler() {
    val TAG = ControllerPost::class.java.simpleName
    override suspend fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        val params = session.parms
        try {
            val controller = getController(params)

            if (controller is ArduinoWritable) {
                val value = params["value"]
                val response = (controller as ArduinoWritable).write(value)
                return NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, "text/json", Gson().toJson(response))
            }

            return getInvalidRequestResponse("post request to non writable controller $controller")
        } catch (e: IllegalArgumentException) {
            return getInvalidRequestResponse(e.message ?: "")
        } catch (e: IOException) {
            Log.d(TAG, "request to arduino web server failed: $e")
            return arduinoHttpError
        }

    }
}
