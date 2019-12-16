//package smarthome.raspberry.arduinodevices.data.server.httphandlers
//
//import fi.iki.elonen.NanoHTTPD
//import fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT
//import smarthome.library.common.BaseController
//import smarthome.library.common.DeviceChannelOutput
//
//internal abstract class BaseRequestHandler() : RequestHandler {
//
//    protected val arduinoHttpError: NanoHTTPD.Response
//        get() = NanoHTTPD.Response(NanoHTTPD.Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "arduino web server not available")
//
//    protected fun getInvalidRequestResponse(message: String): NanoHTTPD.Response {
//        return NanoHTTPD.Response(NanoHTTPD.Response.Status.BAD_REQUEST, MIME_PLAINTEXT, "Invalid request: $message")
//    }
//
//    protected suspend fun getController(params: Map<String, String>): BaseController {
//        val controllerGuid = params.getValue("controller_guid").toLong()
//        return output.findController(controllerGuid)
//    }
//}
