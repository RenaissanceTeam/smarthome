package smarthome.raspberry.arduinodevices.data.mapper

import smarthome.library.common.ControllerState

interface ValuePayloadToControllerStateMapper {
    fun map(value: String): ControllerState
}