package smarthome.raspberry.arduinodevices.data.server.nano

import fi.iki.elonen.NanoHTTPD

typealias NanoRequestDelegate = (NanoHTTPD.IHTTPSession) -> NanoHTTPD.Response

class DelegatableNanoHttpd(port: Int = 8080) : NanoHTTPD(port) {
    private var onRequest: NanoRequestDelegate? = null
    
    override fun serve(session: IHTTPSession?): Response {
        session ?: return makeInternalErrorResponse("Null session")
        
        return onRequest?.let { it(session) } ?: makeInternalErrorResponse("Delegate not set")
    }
    
    fun setDelegate(delegate: NanoRequestDelegate) {
        onRequest = delegate
    }
}

