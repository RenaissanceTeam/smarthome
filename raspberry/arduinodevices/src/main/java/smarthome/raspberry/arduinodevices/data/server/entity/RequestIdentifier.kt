package smarthome.raspberry.arduinodevices.data.server.entity

import fi.iki.elonen.NanoHTTPD

data class RequestIdentifier(
    val method: NanoHTTPD.Method,
    val uri: String
)