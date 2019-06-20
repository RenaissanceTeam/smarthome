package smarthome.raspberry.server.httphandlers

import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.model.SmartHomeRepository

class ResetPost : BaseRequestHandler() {
    override suspend fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        SmartHomeRepository.removeAll()
        return NanoHTTPD.Response("Everything is deleted")
    }
}
