package smarthome.raspberry.arduinodevices.server.httphandlers

import fi.iki.elonen.NanoHTTPD
import smarthome.library.common.ControllerState
import smarthome.library.common.DeviceChannelOutput
import smarthome.raspberry.arduinodevices.ArduinoControllerResponse
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.StringValueState
import smarthome.raspberry.arduinodevices.StringValueStateParser
import smarthome.raspberry.arduinodevices.controllers.ArduinoController
import smarthome.raspberry.arduinodevices.controllers.StateParser

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


        val controllers = listOf<ArduinoController>()
        val device = ArduinoDevice(name, description, controllers, ip = ip)
        parseControllers(rawServices, servicesNames, device)

        output.onNewDevice(device)
        return true
    }

    private fun parseControllers(rawServices: List<String>, serviceNames: List<String>, device: ArduinoDevice): List<ArduinoController> {
        val controllers = mutableListOf<ArduinoController>()

        for (i in rawServices.indices) {
            val id = Integer.parseInt(rawServices[i].trim { it <= ' ' })
            val name = serviceNames[i]

            val controller = ArduinoController(name, device, i, StringValueStateParser())
            controllers.add(controller)
        }

        return controllers
    }
}
