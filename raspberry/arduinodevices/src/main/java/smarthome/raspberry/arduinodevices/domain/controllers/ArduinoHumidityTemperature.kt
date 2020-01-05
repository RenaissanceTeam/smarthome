package smarthome.raspberry.arduinodevices.domain.controllers

import smarthome.library.common.BaseController
import smarthome.library.common.Id
import smarthome.raspberry.arduinodevices.domain.state.IntValueState

class ArduinoHumidityTemperature(id: Id, name: String, state: IntValueState) :
    BaseController(id = id, name = name, state = state)