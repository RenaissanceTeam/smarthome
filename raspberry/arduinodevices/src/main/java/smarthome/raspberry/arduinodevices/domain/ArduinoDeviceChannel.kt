package smarthome.raspberry.arduinodevices.domain


import smarthome.raspberry.arduinodevices.data.ArduinoDeviceAddressRepository
import smarthome.raspberry.arduinodevices.data.ArduinoDeviceApi
import smarthome.raspberry.arduinodevices.data.ArduinoDeviceApiFactory
import smarthome.raspberry.entity.Controller
import smarthome.raspberry.entity.DeviceChannel

class ArduinoDeviceChannel(
        private val addressRepository: ArduinoDeviceAddressRepository,
        private val arduinoDeviceApiFactory: ArduinoDeviceApiFactory
) : DeviceChannel {
    override fun canWorkWith(type: String) = type == "arduino"

    override fun read(controller: Controller): String {
        val controllerIndex = controller.device.controllers.indexOf(controller)
        return getApi(controller).readController(controllerIndex)
    }

    private fun getApi(controller: Controller): ArduinoDeviceApi {
        val address = addressRepository.findByDevice(controller.device).address
        return arduinoDeviceApiFactory.getForAddress(address)
    }

    override fun write(controller: Controller, state: String): String {
        val controllerIndex = controller.device.controllers.indexOf(controller)
        return getApi(controller).writeStateToController(controllerIndex, state)
    }
}

