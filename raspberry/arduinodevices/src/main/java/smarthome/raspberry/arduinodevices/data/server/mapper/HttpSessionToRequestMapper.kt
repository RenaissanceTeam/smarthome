package smarthome.raspberry.arduinodevices.data.server.mapper

import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.arduinodevices.data.server.entity.Request
import smarthome.raspberry.arduinodevices.data.server.entity.RequestIdentifier

interface HttpSessionToRequestMapper {
    fun map(session: NanoHTTPD.IHTTPSession): Request
}

