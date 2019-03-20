package smarthome.client.fragments.controllerdetail.statechanger

import smarthome.library.common.ControllerType


object ControllerTypeAdapter {

    fun toStateChangerType(type: String): StateChangerType {
        return when (type) {

            ControllerType.ARDUINO_ON_OFF.toString() -> StateChangerType.ONOFF
            else -> StateChangerType.ONLY_READ
        }
    }
}


