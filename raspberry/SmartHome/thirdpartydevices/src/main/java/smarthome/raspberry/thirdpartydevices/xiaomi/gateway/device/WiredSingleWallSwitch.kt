package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device

import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.WIRED_SINGLE_WALL_SWITCH_TYPE
import smarthome.library.common.constants.GATEWAY_SINGLE_BUTTON_CONTROLLER
import smarthome.library.common.constants.STATUS_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.ButtonController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport

class WiredSingleWallSwitch(sid: String, transport: UdpTransport, gatewaySid: String)
    : GatewayDevice(sid, WIRED_SINGLE_WALL_SWITCH_TYPE, gatewaySid) {

    init {
        addControllers(ButtonController(this, GATEWAY_SINGLE_BUTTON_CONTROLLER, transport))
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            if (!o.isNull(STATUS_KEY))
                getControllerByType("left").state = o.getString(STATUS_KEY)

            super.parseData(json)
        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }

    override fun toString(): String {
        return super.toString() + "\nstatus: " + getControllerByType("left").state
    }
}