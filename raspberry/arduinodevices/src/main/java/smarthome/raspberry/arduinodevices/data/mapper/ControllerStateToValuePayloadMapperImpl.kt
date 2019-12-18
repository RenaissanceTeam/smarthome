package smarthome.raspberry.arduinodevices.data.mapper

import smarthome.library.common.ControllerState
import smarthome.raspberry.arduinodevices.domain.state.StringValueState

class ControllerStateToValuePayloadMapperImpl : ControllerStateToValuePayloadMapper {
    override fun map(state: ControllerState): String {
        return when (state) {
            is StringValueState -> state.value
            else -> ""
        }
    }
}