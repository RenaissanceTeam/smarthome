package smarthome.raspberry.arduinodevices.data.server.httphandlers

import smarthome.raspberry.arduinodevices.data.server.api.RequestHandler
import smarthome.raspberry.arduinodevices.data.server.entity.*
import smarthome.raspberry.arduinodevices.data.server.mapper.JsonDeviceMapper
import smarthome.raspberry.arduinodevices.data.server.takeIfNotEmpty

class InitPost(
    private val deviceMapper: JsonDeviceMapper
) : RequestHandler {
    override val identifier = RequestIdentifier(
        Method.POST,
        "/iot/init",
        parameters = setOf(
            body
        )
    )
    
    override suspend fun serve(request: Request): Response {
        return withCaughtErrors {
            val deviceJson = request.body.takeIf { it.isNotEmpty() } ?: throw BadParams("empty body for init post")
            val device = deviceMapper.map(deviceJson)
        
            success()
        }
    }
    
    
//    override suspend fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
//        return if (initNewArduinoDevice(session)) {
//            NanoHTTPD.Response("Added successfully")
//        } else NanoHTTPD.Response("ArduinoDevice was not added")
//    }
//
//    private suspend fun initNewArduinoDevice(session: NanoHTTPD.IHTTPSession): Boolean {
//        val params = session.parms
//        val name = params.getValue("name")
//        val description = params["description"]
//        val ip = session.headers.getValue("http-client-ip")
//        val rawServices = params.getValue("services").split(';')
//        val servicesNames = params.getValue("names").split(';')
//
//
//        val controllers = listOf<ArduinoController>()
//        val device = ArduinoDevice(name, description, controllers, ip = ip)
//        parseControllers(rawServices, servicesNames, device)
//
//        output.onNewDevice(device)
//        return true
//    }
//
}
