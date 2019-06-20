package smarthome.raspberry.server

import android.util.Log

import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.server.httphandlers.AlertPost
import smarthome.raspberry.server.httphandlers.ControllerGet
import smarthome.raspberry.server.httphandlers.ControllerPost
import smarthome.raspberry.server.httphandlers.ErrorHandler
import smarthome.raspberry.server.httphandlers.InfoGet
import smarthome.raspberry.server.httphandlers.InitPost
import smarthome.raspberry.server.httphandlers.RequestHandler
import smarthome.raspberry.server.httphandlers.ResetPost

import fi.iki.elonen.NanoHTTPD.Method.GET
import fi.iki.elonen.NanoHTTPD.Method.POST

enum class HandlerType private constructor(private val method: NanoHTTPD.Method, private val requestPath: String, private val handler: RequestHandler) {
    READ_CONTROLLER(GET, "/controller", ControllerGet()),
    CHANGE_CONTROLLER(POST, "/controller", ControllerPost()),
    INFO(GET, "/info", InfoGet()),
    INIT(POST, "/init", InitPost()),
    RESET(POST, "/reset", ResetPost()),
    ALERT(POST, "/alert", AlertPost());


    companion object {
        val TAG = HandlerType::class.java.simpleName
        private val errorHandler = ErrorHandler()

        suspend fun handle(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
            val method = session.method
            val uri = session.uri
            Log.d(TAG, "handle: " + method + ":" + uri + " " + session.queryParameterString)

            val handlerType = findSuitableHandler(method, uri)
            return handlerType.handler.serve(session)
        }

        fun errorHandle(message: String): NanoHTTPD.Response {
            return errorHandler.getError(message)
        }

        private fun findSuitableHandler(method: NanoHTTPD.Method, uri: String): HandlerType {
            for (handlerType in values()) {
                if (handlerType.method == method && uri.startsWith(handlerType.requestPath)) {
                    return handlerType
                }
            }

            throw IllegalArgumentException("No handler found for $method, $uri")
        }
    }
}
