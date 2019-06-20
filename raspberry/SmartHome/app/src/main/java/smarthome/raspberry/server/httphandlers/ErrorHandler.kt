package smarthome.raspberry.server.httphandlers

import fi.iki.elonen.NanoHTTPD

class ErrorHandler : BaseRequestHandler() {

    fun getError(message: String): NanoHTTPD.Response {
        return getInvalidRequestResponse(message)
    }

    override suspend fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        throw IllegalArgumentException(ErrorHandler::class.java.simpleName + " can't serve for valid session")
    }
}
