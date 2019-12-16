package smarthome.raspberry.arduinodevices.domain.controllers

import smarthome.library.common.BaseController
import smarthome.raspberry.arduinodevices.domain.ArduinoDevice
import smarthome.raspberry.arduinodevices.domain.state.StateParser

internal class ArduinoController(
    name: String,
    val device: ArduinoDevice,
    val indexInArduinoServicesArray: Int,
    val parser: StateParser
) : BaseController(name)
