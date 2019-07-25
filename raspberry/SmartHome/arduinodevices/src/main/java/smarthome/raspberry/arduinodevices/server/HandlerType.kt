package smarthome.raspberry.arduinodevices.server

import android.util.Log

import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.arduinodevices.server.httphandlers.AlertPost
import smarthome.raspberry.arduinodevices.server.httphandlers.ControllerGet
import smarthome.raspberry.arduinodevices.server.httphandlers.ControllerPost
import smarthome.raspberry.arduinodevices.server.httphandlers.ErrorHandler
import smarthome.raspberry.arduinodevices.server.httphandlers.InitPost
import smarthome.raspberry.arduinodevices.server.httphandlers.RequestHandler

import fi.iki.elonen.NanoHTTPD.Method.GET
import fi.iki.elonen.NanoHTTPD.Method.POST

internal enum class HandlerType(private val method: NanoHTTPD.Method,
                       private val requestPath: String,
                       private val handler: RequestHandler) {
    READ_CONTROLLER(GET, "/controller", ControllerGet()),
    CHANGE_CONTROLLER(POST, "/controller", ControllerPost()),
    INIT(POST, "/init", InitPost()),
    ALERT(POST, "/alert", AlertPost());


    companion object {
        val TAG = HandlerType::class.java.simpleName
        private val errorHandler = ErrorHandler()

        suspend fun handle(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
            val method = session.method
            val uri = session.uri
            Log.d(TAG, "handle: " + method + ":" + uri + " " + session.queryParameterString)

            return findSuitableHandler(method, uri).serve(session)
        }

        fun errorHandle(message: String): NanoHTTPD.Response {
            return errorHandler.getError(message)
        }

        private fun findSuitableHandler(method: NanoHTTPD.Method, uri: String): RequestHandler {
            for (handlerType in values()) {
                if (handlerType.method == method && uri.startsWith(handlerType.requestPath)) {
                    return handlerType.handler
                }
            }

            throw IllegalArgumentException("No handler found for $method, $uri")
        }
    }
}
