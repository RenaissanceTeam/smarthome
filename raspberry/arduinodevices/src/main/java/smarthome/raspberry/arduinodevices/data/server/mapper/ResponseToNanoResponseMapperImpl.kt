package smarthome.raspberry.arduinodevices.data.server.mapper

import fi.iki.elonen.NanoHTTPD
import smarthome.raspberry.arduinodevices.data.server.entity.Response

class ResponseToNanoResponseMapperImpl(
    private val responseCodeToStatusMapper: ResponseCodeToStatusMapper
) : ResponseToNanoResponseMapper {
    
    override fun map(response: Response): NanoHTTPD.Response {
        return NanoHTTPD.Response(
            responseCodeToStatusMapper.map(response.code),
            response.mimeType,
            response.payload
        )
    }
}