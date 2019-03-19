package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.IotDevice
import smarthome.library.common.constants.GATEWAY_VOLTAGE_CONTROLLER
import smarthome.raspberry.thirdpartydevices.BuildConfig
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.IDLE_STATUS
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.VOLTAGE_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.Controller
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.listeners.SmokeAlarmListener
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.listeners.StateChangeListener
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.listeners.WaterLeakListener

abstract class Device(val sid: String,
                      type: String,
                      private val stateChangeListener: StateChangeListener? = null,
                      val smokeAlarmListener: SmokeAlarmListener? = null,
                      val waterLeakListener: WaterLeakListener? = null)
    : IotDevice () {

    var status: String = IDLE_STATUS

    init {
        this.type = type
    }

    abstract fun parseData(json: String)

    fun setVoltage(o: JSONObject) {
        if (!o.isNull(VOLTAGE_KEY))
            getControllerByType(GATEWAY_VOLTAGE_CONTROLLER).state =
                    (o.getString(VOLTAGE_KEY).toFloat() / 1000).toString()
    }

    fun getControllerByType(type: String): Controller {
        TODO()
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