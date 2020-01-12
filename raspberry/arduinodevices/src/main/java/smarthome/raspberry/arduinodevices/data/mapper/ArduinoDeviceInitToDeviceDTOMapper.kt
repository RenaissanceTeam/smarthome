package smarthome.raspberry.arduinodevices.data.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.data.dto.ArduinoDeviceInit
import smarthome.raspberry.devices.api.domain.dto.DeviceDTO

@Component
class ArduinoDeviceInitToDeviceDTOMapper {
    fun map(arduino: ArduinoDeviceInit) = DeviceDTO(
            name = arduino.name,
            serialName = arduino.serialName,
            controllers = arduino.controllers,
            description = arduino.description,
            type = arduino.type
    )
}