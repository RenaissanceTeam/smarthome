package smarthome.raspberry.arduinodevices.data.server.mapper

import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.arduinodevices.data.server.entity.RequestIdentifier

interface HttpSessionToRequestIdentifierMapper {
    fun map(session: NanoHTTPD.IHTTPSession): RequestIdentifier
}
