package smarthome.raspberry.arduinodevices.data.server.api

import smarthome.raspberry.arduinodevices.data.server.entity.RequestIdentifier
import smarthome.raspberry.arduinodevices.data.server.entity.Response

interface RequestHandler {
    val identifier: RequestIdentifier
    
    suspend fun serve(parameters: Map<String, String> = emptyMap()): Response
}

