package smarthome.raspberry.arduinodevices.domain.state

import smarthome.library.common.ControllerState
import smarthome.raspberry.arduinodevices.data.dto.ArduinoControllerResponse

class StringValueStateParser:
    StateParser {
    override fun parse(response: ArduinoControllerResponse): ControllerState {
        return StringValueState(
            response.response)
    }

    override fun encode(state: ControllerState): String {
        return (state as StringValueState).value
    }
}