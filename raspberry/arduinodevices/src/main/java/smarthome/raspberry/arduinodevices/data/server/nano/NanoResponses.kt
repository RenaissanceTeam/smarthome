package smarthome.raspberry.arduinodevices.data.server.nano

import fi.iki.elonen.NanoHTTPD

val RESPONSE_NOT_FOUND = NanoHTTPD.Response(NanoHTTPD.Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Resource not found")
fun makeInternalErrorResponse(message: String) = NanoHTTPD.Response(NanoHTTPD.Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, message)
