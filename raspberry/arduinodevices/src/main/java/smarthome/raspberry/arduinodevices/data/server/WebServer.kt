package smarthome.raspberry.arduinodevices.data.server

import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.*
import kotlinx.coroutines.runBlocking


class WebServerImpl(
    private val nanoHTTPD: NanoHTTPD
    ) : WebServer {
    private val handlers = mutableListOf<RequestHandler>()
    
    override fun setHandler(handler: RequestHandler) {
        handlers.add(handler)
    }
    
    
    override fun serve(request: RequestIdentifier): Response {
        return when (val handler = handlers.find { it.identifier == request }) {
            null -> Response(notFound, MIME_PLAINTEXT, "Resource not found")
            else -> runBlocking { handler.serve() }
        }
    }
}

interface WebServer {

    fun setHandler(handler: RequestHandler)
    fun serve(request: RequestIdentifier): Response

//    override fun serve(session: IHTTPSession): Response {
//        return try {
//            runBlocking { handler.handle(session) }
//        } catch (e: Exception) {
//            Response("error $e")
//        }
//    }
//
//    override fun startServer() {
//        try {
//            start()
//        } catch (e: IOException) {
//
//        }
//    }
//
//    override fun stopServer() {
//        stop()
//    }
}
//
//private class WebHandler(private val output: DeviceChannelOutput) {
//    suspend fun handle(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
//        val method = session.method
//        val uri = session.uri
//
//        return findSuitableHandler(method, uri).serve(session)
//    }
//
//    private fun findSuitableHandler(method: NanoHTTPD.Method, uri: String): RequestHandler {
//
//        if (NanoHTTPD.Method.POST == method && uri.startsWith("/alert")) {
//            return AlertPost(output)
//        }
//        return InitPost(output)
//    }
//}
