package smarthome.raspberry.arduinodevices.domain


import smarthome.raspberry.arduinodevices.data.repository.ArduinoDeviceAddressRepository
import smarthome.raspberry.arduinodevices.data.api.ArduinoDeviceApi
import smarthome.raspberry.arduinodevices.data.api.ArduinoDeviceApiFactory
import smarthome.raspberry.arduinodevices.util.index
import smarthome.raspberry.entity.controller.Controller
import smarthome.raspberry.channel.api.domain.entity.DeviceChannel

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

