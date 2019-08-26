package smarthome.raspberry.arduinodevices.controllers

import smarthome.library.common.ControllerState
import smarthome.raspberry.arduinodevices.ArduinoControllerResponse

internal interface StateParser {
    fun parse(response: ArduinoControllerResponse): ControllerState

    fun encode(state: ControllerState): String
}