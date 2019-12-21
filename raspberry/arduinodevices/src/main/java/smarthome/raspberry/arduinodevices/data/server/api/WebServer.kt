package smarthome.raspberry.arduinodevices.data.server.api


interface WebServer {
    fun setHandler(handler: RequestHandler)
    fun start()
    fun stop()

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
