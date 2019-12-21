package smarthome.raspberry.arduinodevices.domain.state

import smarthome.library.common.ControllerState
import smarthome.raspberry.arduinodevices.data.dto.ArduinoControllerResponse

internal interface StateParser {
    fun parse(response: ArduinoControllerResponse): ControllerState

    fun encode(state: ControllerState): String
}