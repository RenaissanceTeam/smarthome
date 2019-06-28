package smarthome.raspberry.arduinodevices.server.httphandlers

import android.util.Log
import com.google.gson.Gson
import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.arduinodevices.ArduinoDeviceChannel
import smarthome.raspberry.arduinodevices.server.body
import java.io.IOException

internal class ControllerPost : BaseRequestHandler() {
    private val TAG = ControllerPost::class.java.simpleName
    private val channel: ArduinoDeviceChannel = TODO()

    override suspend fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        val params = session.parms
        val controller = getController(params)
        val device = input.findDevice(controller)

        return try {
            val state = channel.writeState(device, controller, session.body())
            NanoHTTPD.Response(
                    NanoHTTPD.Response.Status.OK,
                    "text/json",
                    Gson().toJson(state)
            )
        } catch (e: IllegalArgumentException) {
            getInvalidRequestResponse(e.message ?: "")
        } catch (e: IOException) {
            Log.d(TAG, "request to arduino web server failed: $e")
            arduinoHttpError
        }
    }
}
