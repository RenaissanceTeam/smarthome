package smarthome.raspberry.arduinodevices.data.server.nano

import smarthome.raspberry.arduinodevices.data.server.api.WebServerGate
import smarthome.raspberry.arduinodevices.data.server.entity.Request
import smarthome.raspberry.arduinodevices.data.server.entity.Response
import smarthome.raspberry.arduinodevices.data.server.mapper.HttpSessionToRequestMapper
import smarthome.raspberry.arduinodevices.data.server.mapper.ResponseToNanoResponseMapper

class NanoHttpdToWebServerAdapter(
    private val nanoHttpd: DelegatableNanoHttpd,
    private val sessionMapper: HttpSessionToRequestMapper,
    private val responseMapper: ResponseToNanoResponseMapper
) : WebServerGate {
    
    override fun start() {
        nanoHttpd.start()
    }
    
    override fun stop() {
        nanoHttpd.stop()
    }
    
    override fun setOnRequest(action: (Request) -> Response) {
        nanoHttpd.setDelegate {
            val response = action(sessionMapper.map(it))
            responseMapper.map(response)
        }
    }
    
}