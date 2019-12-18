package smarthome.raspberry.arduinodevices.data.mapper

import smarthome.library.common.ControllerState
import smarthome.raspberry.arduinodevices.data.mapper.ValuePayloadToControllerStateMapper
import smarthome.raspberry.arduinodevices.domain.state.StringValueState

class ValuePayloadToControllerStateMapperImpl :
    ValuePayloadToControllerStateMapper {
    override fun map(value: String): ControllerState {
        return StringValueState(value)
    }
}