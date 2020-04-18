package smarthome.raspberry.arduinodevices.domain.usecases

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.data.repository.ArduinoDeviceAddressRepository
import smarthome.raspberry.arduinodevices.domain.dto.ArduinoDeviceInit
import smarthome.raspberry.arduinodevices.domain.entity.ArduinoController
import smarthome.raspberry.arduinodevices.domain.entity.ArduinoControllerRepository
import smarthome.raspberry.arduinodevices.domain.entity.ArduinoDeviceAddress
import smarthome.raspberry.arduinodevices.domain.mapper.ArduinoDeviceInitToDeviceDTOMapper
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
                        addressRepository.save(ArduinoDeviceAddress(device = device, address = ip))
                    }
                }
    }
}