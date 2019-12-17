package smarthome.raspberry.arduinodevices.data.server.mapper

import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.arduinodevices.data.server.entity.Method

class NanoMethodToMethodMapperImpl : NanoMethodToMethodMapper {
    override fun map(method: NanoHTTPD.Method): Method {
        return when (method) {
            NanoHTTPD.Method.GET -> Method.GET
            NanoHTTPD.Method.POST -> Method.POST
            else -> throw IllegalArgumentException("method $method cannot be mapped")
        }
    }
}