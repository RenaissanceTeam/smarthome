package smarthome.raspberry.arduinodevices.domain.controllers

import smarthome.library.common.BaseController
import smarthome.library.common.Id
import smarthome.raspberry.arduinodevices.domain.state.BooleanValueState

class ArduinoOnOff(id: Id, name: String, state: BooleanValueState) :
    BaseController(id = id, name = name, state = state), Writeable