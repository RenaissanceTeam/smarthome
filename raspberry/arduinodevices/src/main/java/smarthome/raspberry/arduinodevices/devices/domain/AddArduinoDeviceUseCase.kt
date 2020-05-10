package smarthome.raspberry.arduinodevices.devices.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.controllers.domain.dto.ArduinoDeviceAddressRepository
import smarthome.raspberry.arduinodevices.devices.domain.dto.ArduinoDeviceInit
import smarthome.raspberry.arduinodevices.controllers.domain.entity.ArduinoController
import smarthome.raspberry.arduinodevices.controllers.data.ArduinoControllerRepository
import smarthome.raspberry.arduinodevices.devices.domain.entity.ArduinoDeviceAddress
import smarthome.raspberry.arduinodevices.devices.domain.mapper.ArduinoDeviceInitToDeviceDTOMapper
import smarthome.raspberry.devices.api.domain.AddDeviceUseCase
import smarthome.raspberry.devices.api.domain.GetDeviceBySerialUseCase
import smarthome.raspberry.devices.api.domain.exceptions.DeviceAlreadyExists

@Component
open class AddArduinoDeviceUseCase(
        private val addDeviceUseCase: AddDeviceUseCase,
        private val mapper: ArduinoDeviceInitToDeviceDTOMapper,
        private val addressRepository: ArduinoDeviceAddressRepository,
        private val controllerRepository: ArduinoControllerRepository,
        private val getDeviceBySerialUseCase: GetDeviceBySerialUseCase
) {

    fun execute(ip: String, arduino: ArduinoDeviceInit) {
        addDeviceUseCase.runCatching { execute(mapper.map(arduino)) }
                .onSuccess { device ->
                    addressRepository.save(ArduinoDeviceAddress(device = device, address = ip))

                    device.controllers.zip(arduino.services)
                            .map { ArduinoController(controller = it.first, serial = it.second.serial) }
                            .forEach { controllerRepository.save(it) }
                }
                .onFailure {
                    if (it is DeviceAlreadyExists) {
                        val device = getDeviceBySerialUseCase.execute(arduino.serial) ?: return
                        val updatedAddress = addressRepository.findByDevice(device)?.copy(address = ip) ?: return

                        addressRepository.save(updatedAddress)
                    }
                }
    }
}