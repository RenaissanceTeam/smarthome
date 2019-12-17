package smarthome.raspberry.arduinodevices.data.server.api

import smarthome.raspberry.arduinodevices.data.server.entity.RequestIdentifier
import smarthome.raspberry.arduinodevices.data.server.entity.Response

interface WebServerGate {
    fun start()
    fun stop()
    fun setOnRequest(action: (RequestIdentifier, params: Map<String, String>) -> Response)
}