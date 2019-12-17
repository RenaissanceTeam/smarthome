package smarthome.raspberry.arduinodevices.data.server

import kotlinx.coroutines.runBlocking
import smarthome.raspberry.arduinodevices.data.server.api.RequestHandler
import smarthome.raspberry.arduinodevices.data.server.api.WebServer
import smarthome.raspberry.arduinodevices.data.server.api.WebServerGate
import smarthome.raspberry.arduinodevices.data.server.entity.RequestIdentifier
import smarthome.raspberry.arduinodevices.data.server.entity.Response
import smarthome.raspberry.arduinodevices.data.server.entity.notFound

class WebServerImpl(
    private val gate: WebServerGate
) : WebServer {
    private val handlers = mutableMapOf<RequestIdentifier, RequestHandler>()
    
    override fun setHandler(handler: RequestHandler) {
        handlers[handler.identifier] = handler
    }
    
    override fun start() {
        gate.setOnRequest(::serve)
        gate.start()
    }
    
    override fun stop() {
        gate.stop()
    }
    
    private fun serve(request: RequestIdentifier, params: Map<String, String>): Response {
        return when (val handler = handlers[request]) {
            null -> notFound
            else -> {
                val keysExpected = handler.identifier.parameters
                if (!params.keys.containsAll(keysExpected)) return notFound
                
                runBlocking { handler.serve(params) }
            }
        }
    }
}

