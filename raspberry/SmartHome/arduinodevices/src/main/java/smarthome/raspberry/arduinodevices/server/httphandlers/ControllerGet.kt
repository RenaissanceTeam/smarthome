package smarthome.raspberry.arduinodevices.server.httphandlers

import com.google.gson.Gson
import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.arduinodevices.ArduinoDeviceChannel

internal class ControllerGet : BaseRequestHandler() {
    private val channel: ArduinoDeviceChannel = TODO()

    override suspend fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        val params = session.parms
        val controller = getController(params)
        val device = input.findDevice(controller)

        val state = channel.read(device, controller)
        return NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, "text/json",
                Gson().toJson(state))
    }
}
