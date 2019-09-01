package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants

import android.util.Log
import smarthome.raspberry.thirdpartydevices.BuildConfig.DEBUG
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.WriteCommandListener

// Yeelight device services
const val GET_PROPERTY_METHOD_HEADER = "get_prop"

val defWriteCommandListener: WriteCommandListener = WriteCommandListener {
    if (DEBUG) {
        if(it.isOkResult())
            Log.d("WriteCommandListener", "the write was successful")
        else Log.d("WriteCommandListener", "write failed", it.getException())
    }
}