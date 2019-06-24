package smarthome.raspberry.arduinodevices.server.httphandlers

import fi.iki.elonen.NanoHTTPD

interface RequestHandler {
    suspend fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response
}
