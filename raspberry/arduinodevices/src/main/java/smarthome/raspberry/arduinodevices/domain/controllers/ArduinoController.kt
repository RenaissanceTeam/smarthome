package smarthome.raspberry.arduinodevices.domain.controllers

import smarthome.library.common.BaseController
import smarthome.raspberry.arduinodevices.domain.ArduinoDevice

internal class ArduinoController(
    name: String,
    val device: ArduinoDevice,
    val indexInArduinoServicesArray: Int
) : BaseController(name)
