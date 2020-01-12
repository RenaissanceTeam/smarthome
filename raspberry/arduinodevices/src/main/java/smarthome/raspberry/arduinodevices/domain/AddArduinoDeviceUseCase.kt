package smarthome.raspberry.arduinodevices.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.data.ArduinoDeviceAddressRepository
import smarthome.raspberry.arduinodevices.data.dto.ArduinoDeviceInit
import smarthome.raspberry.arduinodevices.data.mapper.ArduinoDeviceInitToDeviceDTOMapper
import smarthome.raspberry.devices.api.domain.AddDeviceUseCase

@Component
open class AddArduinoDeviceUseCase(
        private val addDeviceUseCase: AddDeviceUseCase,
        private val mapper: ArduinoDeviceInitToDeviceDTOMapper,
        private val addressRepository: ArduinoDeviceAddressRepository
) {

    fun execute(ip: String, arduino: ArduinoDeviceInit) {
        val device = addDeviceUseCase.execute(mapper.map(arduino))

        addressRepository.save(ArduinoDeviceAddress(device = device, address = "$ip:${arduino.port}"))
    }
}