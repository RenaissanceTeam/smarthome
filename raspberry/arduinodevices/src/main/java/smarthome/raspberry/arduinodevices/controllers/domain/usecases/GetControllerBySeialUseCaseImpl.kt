package smarthome.raspberry.arduinodevices.controllers.domain.usecases

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.controllers.data.ArduinoControllerRepository
import smarthome.raspberry.arduinodevices.controllers.domain.entity.ArduinoController
import smarthome.raspberry.util.exceptions.notFound

@Component
class GetControllerBySeialUseCaseImpl(
        private val arduinoControllerRepository: ArduinoControllerRepository
) : GetControllerBySeialUseCase {
    override fun execute(serial: Int): ArduinoController {
        return arduinoControllerRepository.findBySerial(serial) ?: throw notFound
    }
}