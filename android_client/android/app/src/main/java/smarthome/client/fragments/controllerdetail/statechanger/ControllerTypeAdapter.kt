package smarthome.client.fragments.controllerdetail.statechanger

import smarthome.library.common.ControllerType


object ControllerTypeAdapter {

    fun toStateChangerType(type: ControllerType): StateChangerType {
        return when (type) {
            ControllerType.ARDUINO_ON_OFF -> StateChangerType.ONOFF
            else -> StateChangerType.ONLY_READ
        }
    }
}


