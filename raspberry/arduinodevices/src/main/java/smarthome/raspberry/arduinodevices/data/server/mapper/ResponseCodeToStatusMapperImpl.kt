package smarthome.raspberry.arduinodevices.data.server.mapper

import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.arduinodevices.data.server.entity.BAD_REQUEST_CODE
import smarthome.raspberry.arduinodevices.data.server.entity.NOT_FOUND_CODE
import smarthome.raspberry.arduinodevices.data.server.entity.SUCCESS_CODE

class ResponseCodeToStatusMapperImpl : ResponseCodeToStatusMapper {
    override fun map(code: Int): NanoHTTPD.Response.IStatus {
        return when (code) {
            NOT_FOUND_CODE -> NanoHTTPD.Response.Status.NOT_FOUND
            BAD_REQUEST_CODE -> NanoHTTPD.Response.Status.BAD_REQUEST
            SUCCESS_CODE -> NanoHTTPD.Response.Status.OK
            else -> throw IllegalArgumentException("Unknown response code map")
        }
    }
}