package smarthome.raspberry.arduinodevices.data.server.httphandlers

import fi.iki.elonen.NanoHTTPD
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.library.common.DeviceChannelOutput
import smarthome.raspberry.arduinodevices.domain.state.StringValueState

internal class AlertPost(output: DeviceChannelOutput) : BaseRequestHandler(output) {
    private lateinit var value: ControllerState
    private lateinit var controller: BaseController

    override suspend fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        return try {
            parseRequest(session)
            controller.state = value
            output.onNewState(controller)
            NanoHTTPD.Response("alert ok")
        } catch (e: Exception) {
            NanoHTTPD.Response("alert exception $e")
            throw e
        }
    }

    private suspend fun parseRequest(session: NanoHTTPD.IHTTPSession) {
        val params = session.parms
        value = StringValueState(params["value"] ?: throw IllegalArgumentException("no value"))
        controller = getController(params)
    }
}
