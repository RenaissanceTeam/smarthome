package smarthome.raspberry.arduinodevices.domain.state

import smarthome.library.common.ControllerState

class HumidityTemperatureState(val humidity: Double, val temperature: Double) : ControllerState()