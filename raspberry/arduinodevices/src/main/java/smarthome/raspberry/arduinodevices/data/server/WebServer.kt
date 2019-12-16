package smarthome.raspberry.arduinodevices.data.server

import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.runBlocking
import smarthome.library.common.DeviceChannelOutput
import smarthome.raspberry.arduinodevices.data.server.httphandlers.AlertPost
import smarthome.raspberry.arduinodevices.data.server.httphandlers.InitPost
import smarthome.raspberry.arduinodevices.data.server.httphandlers.RequestHandler
import java.io.IOException

internal class WebServer(deviceChannelOutput: DeviceChannelOutput) : NanoHTTPD(8080),
        StoppableServer {
    private val handler = WebHandler(deviceChannelOutput)
    override fun serve(session: IHTTPSession): Response {
        return try {
            runBlocking { handler.handle(session) }
        } catch (e: Exception) {
            Response("error $e")
        }
    }

    override fun startServer() {
        try {
            start()
        } catch (e: IOException) {
        }
    }

    override fun stopServer() {
        stop()
    }
}

private class WebHandler(private val output: DeviceChannelOutput) {
    suspend fun handle(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        val method = session.method
        val uri = session.uri

        return findSuitableHandler(method, uri).serve(session)
    }

    private fun findSuitableHandler(method: NanoHTTPD.Method, uri: String): RequestHandler {

        if (NanoHTTPD.Method.POST == method && uri.startsWith("/alert")) {
            return AlertPost(output)
        }
        return InitPost(output)
    }
}
