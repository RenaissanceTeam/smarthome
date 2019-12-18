package smarthome.raspberry.arduinodevices.data.mapper

import smarthome.library.common.ControllerState

interface ControllerStateToValuePayloadMapper {
    fun map(state: ControllerState): String
}