package smarthome.raspberry.arduinodevices.data.server.mapper

import smarthome.library.common.ControllerState
import smarthome.raspberry.arduinodevices.domain.state.StringValueState

class ValuePayloadToControllerStateMapperImpl : ValuePayloadToControllerStateMapper {
    override fun map(value: String): ControllerState {
        return StringValueState(value)
    }
}