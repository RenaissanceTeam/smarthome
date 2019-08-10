package smarthome.raspberry.arduinodevices.server.httphandlers

import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT
import smarthome.library.common.BaseController
import smarthome.raspberry.arduinodevices.server.DeviceChannelInput
import smarthome.raspberry.arduinodevices.server.WebServerOutput

internal abstract class BaseRequestHandler : RequestHandler {

    protected val output: WebServerOutput = TODO()
    protected val input: DeviceChannelInput = TODO()

    protected val arduinoHttpError: NanoHTTPD.Response
        get() = NanoHTTPD.Response(NanoHTTPD.Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "arduino web server not available")

    protected fun getInvalidRequestResponse(message: String): NanoHTTPD.Response {
        return NanoHTTPD.Response(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Invalid request: $message")
    }

    protected fun getController(params: Map<String, String>): BaseController {
        // todo add checks so it won't crash
        val controllerGuid = params.getValue("controller_guid").toLong()
        return input.findController(controllerGuid)
    }

}
