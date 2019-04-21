package smarthome.raspberry.server.httphandlers

import fi.iki.elonen.NanoHTTPD
import smarthome.library.common.ControllerType
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.controllers.ArduinoController
import smarthome.raspberry.arduinodevices.controllers.ArduinoControllersFactory
import smarthome.raspberry.model.SmartHomeRepository
import java.util.*

class InitPost : BaseRequestHandler() {
    override fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        return if (initNewArduinoDevice(session)) {
            NanoHTTPD.Response("Added successfully")
        } else NanoHTTPD.Response("ArduinoDevice was not added")
    }

    private fun initNewArduinoDevice(session: NanoHTTPD.IHTTPSession): Boolean {
        val params = session.parms
        val name = params["name"]
        val description = params["description"]
        val ip = session.headers["http-client-ip"]
        val rawServices = params["services"]?.split(';')
        val servicesNames = params["names"]?.split(';')


        val device = ArduinoDevice(name, description, ip)
        device.controllers = parseControllers(device, rawServices, servicesNames)

        return SmartHomeRepository.addDevice(device)
    }

    private fun parseControllers(device: ArduinoDevice,
                                 rawServices: List<String>?,
                                 serviceNames: List<String>?): List<ArduinoController> {
        val controllers = ArrayList<ArduinoController>()
        rawServices ?: return listOf()
        serviceNames ?: throw RuntimeException("No service names for services: $rawServices")
        if (rawServices.count() != serviceNames.count()) {
            throw RuntimeException("Count of services=${rawServices.count()} " +
                    "and service names=${serviceNames.count()} is not the same")
        }

        for (i in rawServices.indices) {
            val id = Integer.parseInt(rawServices[i].trim { it <= ' ' })
            val type = ControllerType.getById(id)
            val name = serviceNames[i]

            controllers.add(ArduinoControllersFactory.createArduinoController(type, name, device, i))
        }

        return controllers
    }
}
