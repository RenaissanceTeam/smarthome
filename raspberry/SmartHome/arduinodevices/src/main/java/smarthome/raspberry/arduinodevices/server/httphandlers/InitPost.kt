package smarthome.raspberry.arduinodevices.server.httphandlers

import fi.iki.elonen.NanoHTTPD
import smarthome.library.common.DeviceChannelOutput
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.controllers.ArduinoController
import smarthome.raspberry.arduinodevices.server.WebServerOutput

internal class InitPost(output: DeviceChannelOutput)
    : BaseRequestHandler(output) {
    override suspend fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        return if (initNewArduinoDevice(session)) {
            NanoHTTPD.Response("Added successfully")
        } else NanoHTTPD.Response("ArduinoDevice was not added")
    }

    private suspend fun initNewArduinoDevice(session: NanoHTTPD.IHTTPSession): Boolean {
        val params = session.parms
        val name = params.getValue("name")
        val description = params["description"]
        val ip = session.headers.getValue("http-client-ip")
        val rawServices = params.getValue("services").split(';')
        val servicesNames = params.getValue("names").split(';')


        val controllers = parseControllers(rawServices, servicesNames)
        val device = ArduinoDevice(name, description, controllers, ip = ip)

        output.onNewDevice(device)
        return true
    }

    private fun parseControllers(rawServices: List<String>, serviceNames: List<String>): List<ArduinoController> {
        val controllers = mutableListOf<ArduinoController>()

        for (i in rawServices.indices) {
            val id = Integer.parseInt(rawServices[i].trim { it <= ' ' })
//            val type = ControllerType.getById(id)
            val name = serviceNames[i]

            val controller = createArduinoController()

            controllers.add(controller)
        }

        return controllers
    }

    private fun createArduinoController(): ArduinoController {
        TODO()
    }
}
