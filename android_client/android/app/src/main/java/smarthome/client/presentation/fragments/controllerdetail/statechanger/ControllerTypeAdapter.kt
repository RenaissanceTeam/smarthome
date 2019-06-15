package smarthome.client.presentation.fragments.controllerdetail.statechanger

import smarthome.library.common.ControllerType
import smarthome.library.common.constants.*


object ControllerTypeAdapter {

    fun toStateChangerType(type: String): StateChangerType {
        return when (type) {

            ControllerType.ARDUINO_ON_OFF.toString() -> StateChangerType.ONOFF

            POWER_CONTROLLER_TYPE -> StateChangerType.LEXEME_ONOFF
            GATEWAY_LEFT_BUTTON_CONTROLLER -> StateChangerType.LEXEME_ONOFF
            GATEWAY_RIGHT_BUTTON_CONTROLLER -> StateChangerType.LEXEME_ONOFF
            GATEWAY_SINGLE_BUTTON_CONTROLLER -> StateChangerType.LEXEME_ONOFF
            GATEWAY_LIGHT_ON_OFF_CONTROLLER -> StateChangerType.LEXEME_ONOFF
            GATEWAY_SMART_PLUG_ON_OFF_CONTROLLER -> StateChangerType.LEXEME_ONOFF

            TOGGLE_CONTROLLER_TYPE -> StateChangerType.SIMPLE_WRITE
            DELETE_CRON_CONTROLLER_TYPE -> StateChangerType.SIMPLE_WRITE
            DEFAULT_CONTROLLER_TYPE -> StateChangerType.SIMPLE_WRITE

            BRIGHTNESS_CONTROLLER_TYPE -> StateChangerType.DIMMER

            RGB_CONTROLLER_TYPE -> StateChangerType.RGB
            GATEWAY_RGB_CONTROLLER -> StateChangerType.RGB

            CRON_ADD_CONTROLLER_TYPE -> StateChangerType.DIGITS_DECIMAL_READ_WRITE
            NAME_CONTROLLER_TYPE -> StateChangerType.TEXT_READ_WRITE

            else -> StateChangerType.ONLY_READ
        }
    }
}


