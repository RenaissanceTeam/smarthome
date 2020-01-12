package smarthome.raspberry.arduinodevices.domain


import smarthome.raspberry.arduinodevices.data.ArduinoDeviceAddressRepository
import smarthome.raspberry.arduinodevices.data.ArduinoDeviceApi
import smarthome.raspberry.arduinodevices.data.ArduinoDeviceApiFactory
import smarthome.raspberry.arduinodevices.util.index
import smarthome.raspberry.entity.Controller
import smarthome.raspberry.entity.DeviceChannel

class ArduinoDeviceChannel(
        private val addressRepository: ArduinoDeviceAddressRepository,
        private val arduinoDeviceApiFactory: ArduinoDeviceApiFactory
) : DeviceChannel {
    override fun canWorkWith(type: String) = type == "arduino"

    override fun read(controller: Controller): String {
        return getApi(controller).readController(controller.index)
    }

    private fun getApi(controller: Controller): ArduinoDeviceApi {
        val address = addressRepository.findByDevice(controller.device).address
        return arduinoDeviceApiFactory.getForAddress(address)
    }

    override fun write(controller: Controller, state: String): String {
        return getApi(controller).writeStateToController(controller.index, state)
    }
}

