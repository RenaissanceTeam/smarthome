package smarthome.raspberry.arduinodevices.data.server

import fi.iki.elonen.NanoHTTPD.IHTTPSession
import fi.iki.elonen.NanoHTTPD.Response

interface RequestHandler {
    val identifier: RequestIdentifier
    
    suspend fun serve(): Response
}

