package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants

import android.util.Log
import smarthome.raspberry.thirdpartydevices.BuildConfig
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners.StateChangeListener


const val IDLE_STATUS = "idle"

const val IP_KEY = "ip"
const val RGB_KEY = "rgb"
const val ILLUMINATION_KEY = "illumination"
const val PROTO_VERSION_KEY = "proto_version"

const val STATUS_KEY = "status"
const val STATUS_OPEN = "open"
const val STATUS_CLOSE = "close"
const val STATUS_MOTION = "motion"
const val STATUS_NO_MOTION = "no_motion"
const val STATUS_CLICK = "click"
const val STATUS_DOUBLE_CLICK = "double_click"
const val STATUS_LONG_PRESS = "long_click_press"
const val STATUS_TEMPERATURE = "temperature"
const val STATUS_HUMIDITY = "humidity"
const val STATUS_WATER_LEAK = "leak"
const val STATUS_NO_WATER_LEAK = "no_leak"
const val STATUS_PRESSURE = "pressure"
const val STATUS_CHANNEL_0 = "channel_0"
const val STATUS_CHANNEL_1 = "channel_1"

const val STATUS_ON = "on"
const val STATUS_OFF = "off"

const val VOLTAGE_KEY = "voltage"
const val IN_USE_KEY = "inuse"
const val POWER_CONSUMED_KEY = "power_consumed"
const val LOAD_POWER_KEY = "load_power"
const val ALARM_KEY = "alarm"
const val DENSITY_KEY = "density"

val defStateChangeListener: StateChangeListener = StateChangeListener {
    if (BuildConfig.DEBUG) {
        Log.d("StateChangeListener", "new state: $it")
    }
}