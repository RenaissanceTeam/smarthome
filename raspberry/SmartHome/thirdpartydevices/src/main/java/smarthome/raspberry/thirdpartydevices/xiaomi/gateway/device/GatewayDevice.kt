package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device

import android.util.Log
import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.Expose
import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.BaseController
import smarthome.library.common.GUID
import smarthome.library.common.IotDevice
import smarthome.library.common.constants.GATEWAY_VOLTAGE_CONTROLLER
import smarthome.raspberry.thirdpartydevices.BuildConfig
import smarthome.raspberry.thirdpartydevices.utils.Utils.toJson
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.IDLE_STATUS
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.VOLTAGE_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.Controller
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners.SmokeAlarmListener
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners.StateChangeListener
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners.WaterLeakListener

abstract class GatewayDevice(sid: String,
                             type: String,
                             parentGatewaySid: String = IDLE_STATUS,
                             private val stateChangeListener: StateChangeListener? = null,
                             smokeAlarmListener: SmokeAlarmListener? = null,
                             waterLeakListener: WaterLeakListener? = null)
    : IotDevice () {

    @Exclude @Expose val sid: String = sid
        @Exclude get
    @Exclude @Expose val parentGatewaySid: String = parentGatewaySid
        @Exclude get
    @Exclude @Expose var smokeAlarmListener: SmokeAlarmListener? = smokeAlarmListener
        @Exclude get
    @Exclude @Expose var waterLeakListener: WaterLeakListener? = waterLeakListener
        @Exclude get

    init {
        this.name = sid
        this.type = type
        this.guid = GUID.getInstance().getGuidForIotDevice(this)
    }

    fun recoverControllers() {
        for (controller in controllers)
            (controller as Controller).device = this
    }

    open fun parseData(json: String) {
        for (controller in controllers)
            controller.setUpToDate()
    }

    fun setVoltage(o: JSONObject) {
        if (!o.isNull(VOLTAGE_KEY))
            getControllerByType(GATEWAY_VOLTAGE_CONTROLLER).state =
                    (o.getString(VOLTAGE_KEY).toFloat() / 1000).toString() + "v"
    }

    fun getControllerByType(type: String): BaseController {
        for (controller in getControllers())
            if (controller.type == type)
                return controller

        throw IllegalArgumentException("This device does not have controller with type: $type")
    }

    fun invokeStateListener(state: String, device: GatewayDevice, controller: BaseController) {
        stateChangeListener?.onStateChanged(state, device, controller)
    }

    fun reportDataParseError(e: JSONException) {
        if(BuildConfig.DEBUG) Log.d(type, "parse data error", e)
    }

    override fun toString(): String {
        return "\n--- Xiaomi gateway device --- \ntype: $type, sid: $sid, name: $name"
    }

    override fun gsonned(): String {
        return toJson(this)
    }
}