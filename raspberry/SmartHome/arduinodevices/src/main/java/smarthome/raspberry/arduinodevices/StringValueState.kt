package smarthome.raspberry.arduinodevices

import smarthome.library.common.ControllerState
import smarthome.raspberry.arduinodevices.controllers.StateParser

class StringValueState(val value: String) : ControllerState()


class StringValueStateParser: StateParser {
    override fun parse(response: ArduinoControllerResponse): ControllerState {
        return StringValueState(response.response)
    }

    override fun encode(state: ControllerState): String {
        return (state as StringValueState).value
    }
}