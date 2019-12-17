package smarthome.raspberry.arduinodevices.data.server

import fi.iki.elonen.NanoHTTPD

val RESPONSE_NOT_FOUND = NanoHTTPD.Response(NanoHTTPD.Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Resource not found")
