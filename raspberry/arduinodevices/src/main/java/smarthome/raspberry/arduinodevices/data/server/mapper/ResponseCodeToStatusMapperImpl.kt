package smarthome.raspberry.arduinodevices.data.server.mapper

import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.arduinodevices.data.server.entity.NOT_FOUND_CODE

class ResponseCodeToStatusMapperImpl : ResponseCodeToStatusMapper {
    override fun map(code: Int): NanoHTTPD.Response.IStatus {
        return when (code) {
            NOT_FOUND_CODE -> NanoHTTPD.Response.Status.NOT_FOUND
            else -> throw IllegalArgumentException("Unknown response code map")
        }
    }
}