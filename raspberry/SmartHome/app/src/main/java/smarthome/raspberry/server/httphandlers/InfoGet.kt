package smarthome.raspberry.server.httphandlers

import com.google.gson.GsonBuilder
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT
import smarthome.raspberry.model.SmartHomeRepository

class InfoGet : BaseRequestHandler() {
    private val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    override fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        val serializedHomeState = gson.toJson(SmartHomeRepository)
        return NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, serializedHomeState)
    }
}
