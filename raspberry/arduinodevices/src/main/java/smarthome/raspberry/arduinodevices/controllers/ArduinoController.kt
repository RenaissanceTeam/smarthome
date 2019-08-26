package smarthome.raspberry.arduinodevices.controllers

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.raspberry.arduinodevices.ArduinoDevice

internal class ArduinoController(name: String,
                                          val device: ArduinoDevice,
                                          val indexInArduinoServicesArray: Int,
                                          val parser: StateParser) : BaseController(name)
