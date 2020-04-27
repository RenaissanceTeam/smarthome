package smarthome.raspberry.arduinodevices.controllers.domain.usecases

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.controllers.domain.entity.ArduinoController
import smarthome.raspberry.controllers.api.domain.SaveControllerUseCase

@Component
class UpdateArduinoControllerUseCaseImpl(
        private val getControllerBySeialUseCase: GetControllerBySeialUseCase,
        private val saveControllerUseCase: SaveControllerUseCase
) : UpdateArduinoControllerUseCase {
    override fun execute(serial: Int, state: String): ArduinoController {
        val saved = getControllerBySeialUseCase.execute(serial)

        saved.controller.copy(state = state).let(saveControllerUseCase::execute)

        return saved
    }
}