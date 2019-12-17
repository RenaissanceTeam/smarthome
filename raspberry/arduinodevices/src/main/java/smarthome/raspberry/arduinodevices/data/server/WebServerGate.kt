package smarthome.raspberry.arduinodevices.data.server

import smarthome.raspberry.arduinodevices.data.server.entity.RequestIdentifier
import smarthome.raspberry.arduinodevices.data.server.entity.Response

interface WebServerGate {
    fun start()
    fun stop()
    fun setOnRequest(action: (RequestIdentifier) -> Response)
}