package smarthome.library.common

import smarthome.library.common.constants.ControllerIds.*

enum class ControllerType private constructor(val id: Int) {
    ARDUINO_ANALOG(A_ANALOG),
    ARDUINO_ON_OFF(A_ONOFF),
    TEMPERATURE_DHT11(A_TEMPERATURE_DHT11),
    TEMPERATURE_DHT22(A_TEMPERATURE_DHT22),
    HUMIDITY_DHT11(A_HUMIDITY_DHT_11),
    HUMIDITY_DHT22(A_HUMIDITY_DHT_22),
    DIGITAL_ALERT(A_DIGITAL_ALERT),
    INIT(A_INIT),
    PRESSURE(A_PRESSURE);


    companion object {

        fun getById(id: Int): ControllerType {
            for (type in values()) {
                if (type.id == id) {
                    return type
                }
            }
            throw IllegalArgumentException("No such controller type=$id")
        }
    }
}
