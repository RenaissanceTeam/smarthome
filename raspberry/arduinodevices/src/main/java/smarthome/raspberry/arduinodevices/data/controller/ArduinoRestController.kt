package smarthome.raspberry.arduinodevices.data.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import smarthome.raspberry.arduinodevices.domain.dto.ArduinoDeviceInit
import smarthome.raspberry.arduinodevices.domain.usecases.AddArduinoDeviceUseCase
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("iot/api/arduino/")
open class ArduinoRestController(
        private val addArduinoDeviceUseCase: AddArduinoDeviceUseCase
) {

    @PostMapping("init")
    fun init(@RequestBody device: ArduinoDeviceInit, request: HttpServletRequest) {
        addArduinoDeviceUseCase.execute(request.remoteAddr, device)
    }
}