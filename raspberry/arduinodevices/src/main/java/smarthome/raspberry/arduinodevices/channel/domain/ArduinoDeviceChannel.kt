package smarthome.raspberry.arduinodevices.channel.domain


import smarthome.raspberry.arduinodevices.controllers.data.api.ArduinoDeviceApi
import smarthome.raspberry.arduinodevices.controllers.data.api.ArduinoDeviceApiFactory
import smarthome.raspberry.arduinodevices.controllers.domain.dto.ArduinoDeviceAddressRepository
import smarthome.raspberry.arduinodevices.controllers.domain.mapper.ArduinoStateMapper
import smarthome.raspberry.arduinodevices.util.index
import smarthome.raspberry.channel.api.domain.entity.DeviceChannel
import smarthome.raspberry.entity.controller.Controller

class ArduinoDeviceChannel(
        private val addressRepository: ArduinoDeviceAddressRepository,
        private val arduinoDeviceApiFactory: ArduinoDeviceApiFactory,
        private val stateMapper: ArduinoStateMapper
) : DeviceChannel {
    override fun canWorkWith(type: String) = type == "arduino"

    override fun read(controller: Controller): String {
        return getApi(controller).readController(controller.index)
                .let { stateMapper.mapFromRaw(controller, it) }
    }

    private fun getApi(controller: Controller): ArduinoDeviceApi {
        val device = addressRepository.findByDevice(controller.device)
        return arduinoDeviceApiFactory.getForAddress(device.address, device.port)
    }

    override fun write(controller: Controller, state: String): String {
        val writeState = stateMapper.mapToRaw(controller, state)
        return getApi(controller).writeStateToController(controller.index, writeState)
                .let { stateMapper.mapFromRaw(controller, it) }
    }
}

