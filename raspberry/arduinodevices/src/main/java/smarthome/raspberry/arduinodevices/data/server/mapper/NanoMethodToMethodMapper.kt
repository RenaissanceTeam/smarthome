package smarthome.raspberry.arduinodevices.data.server.mapper

import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.arduinodevices.data.server.entity.Method

interface NanoMethodToMethodMapper {
    fun map(method: NanoHTTPD.Method): Method
}