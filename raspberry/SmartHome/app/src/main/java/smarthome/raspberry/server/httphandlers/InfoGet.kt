package smarthome.raspberry.server.httphandlers

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.model.SmartHomeRepository

import fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT

class InfoGet : BaseRequestHandler() {
    private val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    override fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        val serializedHomeState = gson.toJson(SmartHomeRepository)
        return NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, MIME_PLAINTEXT, serializedHomeState)
    }
}
