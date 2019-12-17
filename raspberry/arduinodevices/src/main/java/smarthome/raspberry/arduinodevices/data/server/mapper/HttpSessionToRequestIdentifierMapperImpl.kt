package smarthome.raspberry.arduinodevices.data.server.mapper

import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.arduinodevices.data.server.entity.RequestIdentifier

class HttpSessionToRequestIdentifierMapperImpl : HttpSessionToRequestIdentifierMapper {
    override fun map(session: NanoHTTPD.IHTTPSession): RequestIdentifier {
        return RequestIdentifier(
            session.method,
            session.uri
        )
    }
}