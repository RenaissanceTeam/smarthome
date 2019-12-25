package smarthome.raspberry.arduinodevices.domain.controllers

import smarthome.library.common.BaseController
import smarthome.library.common.Id
import smarthome.raspberry.arduinodevices.domain.state.DoubleValueState

class ArduinoAnalogue(id: Id, name: String, state: DoubleValueState) :
    BaseController(id = id, name = name, state = state)

