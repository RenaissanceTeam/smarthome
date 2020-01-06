package smarthome.raspberry.arduinodevices.data.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import smarthome.raspberry.devices.api.domain.AddDeviceUseCase
import smarthome.raspberry.devices.api.domain.dto.DeviceDTO

@RestController
@RequestMapping("api/arduino/")
open class ArduinoController(
    private val addDeviceUseCase: AddDeviceUseCase
) {

    @PostMapping("init")
    fun init(device: DeviceDTO) {
        addDeviceUseCase.execute(device)
    }
}