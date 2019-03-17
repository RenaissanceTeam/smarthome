package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants

import android.util.Log
import smarthome.raspberry.thirdpartydevices.BuildConfig.DEBUG
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.WriteCommandListener

// Yeelight device services
const val GET_PROPERTY_METHOD_HEADER = "get_prop"

const val ADJUST_CONTROLLER_TYPE = "set_adjust"
const val BRIGHTNESS_CONTROLLER_TYPE = "set_bright"
const val COLOR_TEMPERATURE_CONTROLLER_TYPE = "set_ct_abx"
const val CRON_ADD_CONTROLLER_TYPE = "cron_add"
const val DELETE_CRON_CONTROLLER_TYPE = "cron_del"
const val DEFAULT_CONTROLLER_TYPE = "set_default"
const val FLOW_CONTROLLER_TYPE = "not_supported_in_this_lib_version"
const val HSV_CONTROLLER_TYPE = "set_hsv"
const val NAME_CONTROLLER_TYPE = "set_name"
const val POWER_CONTROLLER_TYPE = "set_power"
const val RGB_CONTROLLER_TYPE = "set_rgb"
const val TOGGLE_CONTROLLER_TYPE = "toggle"

val defWriteCommandListener: WriteCommandListener = WriteCommandListener {
    if (DEBUG) {
        if(it.isOkResult())
            Log.d("WriteCommandListener", "the write was successful")
        else Log.d("WriteCommandListener", "write failed", it.getException())
    }
}