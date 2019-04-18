package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants

import android.util.Log
import smarthome.raspberry.thirdpartydevices.BuildConfig
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners.StateChangeListener


const val IDLE_STATUS = "idle"

const val IP_KEY = "ip"
const val RGB_KEY = "rgb"
const val ILLUMINATION_KEY = "illumination"
const val PROTO_VERSION_KEY = "proto_version"

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