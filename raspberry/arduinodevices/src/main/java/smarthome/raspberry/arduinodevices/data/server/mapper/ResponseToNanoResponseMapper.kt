package smarthome.raspberry.arduinodevices.data.server.mapper

import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.arduinodevices.data.server.entity.Response

interface ResponseToNanoResponseMapper {
    fun map(response: Response): NanoHTTPD.Response
}