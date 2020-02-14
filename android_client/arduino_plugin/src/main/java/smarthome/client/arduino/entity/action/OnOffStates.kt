package smarthome.client.arduino.entity.action

const val on = "on"
const val off = "off"

fun getStateFromBoolean(bool: Boolean) = when (bool) {
    true -> on
    false -> off
}