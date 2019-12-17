package smarthome.raspberry.arduinodevices.data.server.nano

import smarthome.raspberry.arduinodevices.data.server.api.WebServerGate
import smarthome.raspberry.arduinodevices.data.server.entity.RequestIdentifier
import smarthome.raspberry.arduinodevices.data.server.entity.Response
import smarthome.raspberry.arduinodevices.data.server.mapper.HttpSessionToRequestIdentifierMapper
import smarthome.raspberry.arduinodevices.data.server.mapper.ResponseToNanoResponseMapper

class NanoHttpdToWebServerAdapter(
    private val nanoHttpd: DelegatableNanoHttpd,
    private val sessionMapper: HttpSessionToRequestIdentifierMapper,
    private val responseMapper: ResponseToNanoResponseMapper
) : WebServerGate {
    private var action: ((RequestIdentifier) -> Response)? = null
    
    override fun start() {
        nanoHttpd.start()
    }
    
    override fun stop() {
        nanoHttpd.stop()
    }
    
    override fun setOnRequest(action: (RequestIdentifier) -> Response) {
        nanoHttpd.setDelegate {
            val response = action(sessionMapper.map(it))
            responseMapper.map(response)
        }
    }
}