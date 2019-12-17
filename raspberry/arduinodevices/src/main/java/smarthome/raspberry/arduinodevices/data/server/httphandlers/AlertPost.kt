package smarthome.raspberry.arduinodevices.data.server.httphandlers

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.raspberry.arduinodevices.data.server.api.RequestHandler
import smarthome.raspberry.arduinodevices.data.server.entity.Method
import smarthome.raspberry.arduinodevices.data.server.entity.RequestIdentifier
import smarthome.raspberry.arduinodevices.data.server.entity.Response
import smarthome.raspberry.arduinodevices.domain.state.StringValueState

class AlertPost: RequestHandler {
    override val identifier = RequestIdentifier(Method.POST, "/iot/alert")
    
    override suspend fun serve(parameters: Map<String, String>): Response {
        TODO()
    }
    
    //    private lateinit var value: ControllerState
//    private lateinit var controller: BaseController

//    override suspend fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
//        return try {
//            parseRequest(session)
//            controller.state = value
//            output.onNewState(controller)
//            NanoHTTPD.Response("alert ok")
//        } catch (e: Exception) {
//            NanoHTTPD.Response("alert exception $e")
//            throw e
//        }
//    }
//
//    private suspend fun parseRequest(session: NanoHTTPD.IHTTPSession) {
//        val params = session.parms
//        value = StringValueState(params["value"] ?: throw IllegalArgumentException("no value"))
//        controller = getController(params)
//    }
}
