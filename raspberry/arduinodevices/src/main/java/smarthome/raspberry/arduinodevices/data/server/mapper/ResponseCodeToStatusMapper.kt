package smarthome.raspberry.arduinodevices.data.server.mapper

import fi.iki.elonen.NanoHTTPD

interface ResponseCodeToStatusMapper {
    fun map(code: Int): NanoHTTPD.Response.IStatus
}