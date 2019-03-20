package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.BaseController
import smarthome.library.common.GUID
import smarthome.library.common.IotDevice
import smarthome.library.common.constants.GATEWAY_VOLTAGE_CONTROLLER
import smarthome.raspberry.thirdpartydevices.BuildConfig
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.IDLE_STATUS
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.VOLTAGE_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners.SmokeAlarmListener
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners.StateChangeListener
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners.WaterLeakListener

abstract class Device(val sid: String,
                      type: String,
                      private val stateChangeListener: StateChangeListener? = null,
                      var smokeAlarmListener: SmokeAlarmListener? = null,
                      var waterLeakListener: WaterLeakListener? = null)
    : IotDevice () {

    var status: String = IDLE_STATUS

    init {
        this.name = sid
        this.type = type
        this.guid = GUID.getInstance().getGuidForIotDevice(this)
    }

    abstract fun parseData(json: String)

    fun setVoltage(o: JSONObject) {
        if (!o.isNull(VOLTAGE_KEY))
            getControllerByType(GATEWAY_VOLTAGE_CONTROLLER).state =
                    (o.getString(VOLTAGE_KEY).toFloat() / 1000).toString()
    }

    fun getControllerByType(type: String): BaseController {
        for (controller in getControllers())
            if (controller.type == type)
                return controller

        throw IllegalArgumentException("This device does not have controller with type: $type")
    }

    fun invokeStateListener(state: String) {
        stateChangeListener?.onStateChanged(state)
    }

    fun reportDataParseError(e: JSONException) {
        if(BuildConfig.DEBUG) Log.d(type, "parse data error", e)
    }

    override fun toString(): String {
        return "\n--- Xiaomi gateway device --- \ntype: $type, sid: $sid, name: $name"
    }
}