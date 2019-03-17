package smarthome.raspberry.server.httphandlers

import java.util.ArrayList

import fi.iki.elonen.NanoHTTPD
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerType
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.controllers.ArduinoController
import smarthome.raspberry.arduinodevices.controllers.ArduinoControllersFactory
import smarthome.raspberry.model.SmartHomeRepository

class InitPost : BaseRequestHandler() {
    override fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
        // todo add check if it's really arduino if other devices will be added the same way
        return if (initNewArduinoDevice(session)) {
            NanoHTTPD.Response("Added successfully")
        } else NanoHTTPD.Response("ArduinoDevice was not added")
    }

    private fun initNewArduinoDevice(session: NanoHTTPD.IHTTPSession): Boolean {
        val params = session.parms
        val name = params["name"]
        val description = params["description"]
        val ip = session.headers["http-client-ip"]
        val rawServices = params["services"]!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val device = ArduinoDevice(name, description, ip)
        device.controllers = parseControllers(device, rawServices)

        return SmartHomeRepository.addDevice(device)
    }

    private fun parseControllers(device: ArduinoDevice, rawServices: Array<String>): List<ArduinoController> {
        val controllers = ArrayList<ArduinoController>()

        for (i in rawServices.indices) {
            val id = Integer.parseInt(rawServices[i].trim { it <= ' ' })
            val type = ControllerType.getById(id)
            controllers.add(ArduinoControllersFactory.createArduinoController(type, device, i))
        }

        return controllers
    }
}
