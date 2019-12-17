package smarthome.raspberry.arduinodevices.data.server.mapper

import smarthome.library.common.ControllerState

interface ValuePayloadToControllerStateMapper {
    fun map(value: String): ControllerState
}