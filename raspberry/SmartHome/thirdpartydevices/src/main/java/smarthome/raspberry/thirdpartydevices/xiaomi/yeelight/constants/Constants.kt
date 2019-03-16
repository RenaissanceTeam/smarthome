package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants

import android.util.Log
import smarthome.raspberry.thirdpartydevices.BuildConfig.DEBUG
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.WriteCommandListener

// Yeelight methods
const val GET_PROPERTY_METHOD_HEADER = "get_prop"

const val HSV_CONTROLLER_TYPE = "hsv"
const val FLOW_CONTROLLER_TYPE = "flow"
const val CRON_CONTROLLER_TYPE = "cron"
const val DELETE_CRON_CONTROLLER_TYPE = "delete_cron"
const val ADJUST_CONTROLLER_TYPE = "delete_cron"

val defWriteCommandListener: WriteCommandListener = WriteCommandListener {
    if (DEBUG) {
        if(it.isOkResult())
            Log.d("WriteCommandListener", "the write was successful")
        else Log.d("WriteCommandListener", "write failed", it.getException())
    }
}