package smarthome.raspberry.arduinodevices.domain.controllers

import smarthome.library.common.BaseController
import smarthome.library.common.Id
import smarthome.raspberry.arduinodevices.domain.ArduinoDevice

abstract class ArduinoController(
    name: String,
    val indexInArduinoServicesArray: Int
) : BaseController(name)
