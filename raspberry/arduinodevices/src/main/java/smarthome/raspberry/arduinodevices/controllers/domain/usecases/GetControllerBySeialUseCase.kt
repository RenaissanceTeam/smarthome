package smarthome.raspberry.arduinodevices.controllers.domain.usecases

import smarthome.raspberry.arduinodevices.controllers.domain.entity.ArduinoController

interface GetControllerBySeialUseCase {
    fun execute(serial: Int): ArduinoController
}