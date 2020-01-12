package smarthome.raspberry.arduinodevices.domain

import smarthome.raspberry.arduinodevices.data.ArduinoDeviceApiFactory
import smarthome.raspberry.arduinodevices.data.ArduinoDeviceIpRepository
import smarthome.raspberry.entity.Controller
import smarthome.raspberry.entity.DeviceChannel

class ArduinoDeviceChannel(
        private val ipRepository: ArduinoDeviceIpRepository,
        private val arduinoDeviceApiFactory: ArduinoDeviceApiFactory
) : DeviceChannel {
    override fun canWorkWith(type: String) = type == "arduino"

    override fun read(controller: Controller): String {
        val ip = ipRepository.findByDevice(controller.device).ip

        val controllerIndex = controller.device.controllers.indexOf(controller)
        return arduinoDeviceApiFactory.getForIp(ip).readController(controllerIndex)
    }

    override fun write(controller: Controller, state: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

