package smarthome.raspberry.arduinodevices.controllers.domain.usecases

import smarthome.raspberry.arduinodevices.controllers.domain.entity.ArduinoController

interface UpdateArduinoControllerUseCase {
    fun execute(serial: Int, state: String): ArduinoController
}