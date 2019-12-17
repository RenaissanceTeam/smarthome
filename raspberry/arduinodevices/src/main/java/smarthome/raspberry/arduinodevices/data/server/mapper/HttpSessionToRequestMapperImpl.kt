package smarthome.raspberry.arduinodevices.data.server.mapper

import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.arduinodevices.data.server.body
import smarthome.raspberry.arduinodevices.data.server.entity.Request
import smarthome.raspberry.arduinodevices.data.server.entity.RequestIdentifier

class HttpSessionToRequestMapperImpl(
    private val methodMapper: NanoMethodToMethodMapper
) : HttpSessionToRequestMapper {
    override fun map(session: NanoHTTPD.IHTTPSession): Request {
        return Request(
            RequestIdentifier(
                method = methodMapper.map(session.method),
                uri = session.uri,
                parameters = session.parms.keys
            ),
            session.parms,
            session.body()
        )
    }
}