package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device

import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.SMART_PLUG_TYPE
import smarthome.library.common.constants.GATEWAY_LOAD_POWER_CONTROLLER
import smarthome.library.common.constants.GATEWAY_POWER_CONSUMED_CONTROLLER
import smarthome.library.common.constants.GATEWAY_SMART_PLUG_ON_OFF_CONTROLLER
import smarthome.library.common.constants.GATEWAY_TIME_IN_USE_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.IN_USE_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.LOAD_POWER_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.POWER_CONSUMED_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.STATUS_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.*
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport

class SmartPlug(sid: String, transport: UdpTransport, gatewaySid: String)
    : GatewayDevice(sid, SMART_PLUG_TYPE, gatewaySid) {

    init {
        addControllers(SmartPlugOnOffController(this, transport),
                TimeInUseController(this),
                PowerConsumedController(this),
                LoadPowerController(this),
                VoltageController(this))
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            if (!o.isNull(STATUS_KEY)) {
                getControllerByType(GATEWAY_SMART_PLUG_ON_OFF_CONTROLLER).state = o.getString(STATUS_KEY)

                invokeStateListener(getControllerByType(GATEWAY_SMART_PLUG_ON_OFF_CONTROLLER).state)
            }

            if (!o.isNull(IN_USE_KEY))
                getControllerByType(GATEWAY_TIME_IN_USE_CONTROLLER).state = o.getInt(IN_USE_KEY).toString()

            if (!o.isNull(POWER_CONSUMED_KEY))
                getControllerByType(GATEWAY_POWER_CONSUMED_CONTROLLER).state = o.getInt(POWER_CONSUMED_KEY).toString() + "w"

            if (!o.isNull(LOAD_POWER_KEY))
                getControllerByType(GATEWAY_LOAD_POWER_CONTROLLER).state = o.getString(LOAD_POWER_KEY) + "v"

            setVoltage(o)

            super.parseData(json)
        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }

    override fun toString(): String {
        return super.toString() +
                "\nin use: " + getControllerByType(GATEWAY_TIME_IN_USE_CONTROLLER).state +
                ", power consumed: " + getControllerByType(GATEWAY_POWER_CONSUMED_CONTROLLER).state +
                "w, load power: " + getControllerByType(GATEWAY_LOAD_POWER_CONTROLLER).state + "v"
    }
}