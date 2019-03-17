package smarthome.raspberry.server.httphandlers

import fi.iki.elonen.NanoHTTPD
import smarthome.library.common.BaseController
import smarthome.raspberry.model.SmartHomeRepository

import fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT

abstract class BaseRequestHandler : RequestHandler {

    protected val arduinoHttpError: NanoHTTPD.Response
        get() = NanoHTTPD.Response(NanoHTTPD.Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "arduino web server not available")

    protected fun getInvalidRequestResponse(message: String): NanoHTTPD.Response {
        return NanoHTTPD.Response(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Invalid request: $message")
    }

    protected fun getController(params: Map<String, String>): BaseController {
        // todo add checks so it won't crash
        val controllerGuid = java.lang.Long.parseLong(params["controller_guid"]!!)
        return SmartHomeRepository.getController(controllerGuid)
    }

}
