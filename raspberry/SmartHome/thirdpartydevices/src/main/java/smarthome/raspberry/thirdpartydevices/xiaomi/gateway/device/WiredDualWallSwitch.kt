package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device

import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.Expose
import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.WIRED_DUAL_WALL_SWITCH_TYPE
import smarthome.library.common.constants.GATEWAY_LEFT_BUTTON_CONTROLLER
import smarthome.library.common.constants.GATEWAY_RIGHT_BUTTON_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.IDLE_STATUS
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.STATUS_CHANNEL_0
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.STATUS_CHANNEL_1
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.ButtonController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport

class WiredDualWallSwitch (sid: String, transport: UdpTransport, gatewaySid: String)
    : GatewayDevice(sid, WIRED_DUAL_WALL_SWITCH_TYPE, gatewaySid) {

    @Exclude @Expose var statusLeft: String = IDLE_STATUS
        @Exclude get
    @Exclude @Expose var statusRight: String = IDLE_STATUS
        @Exclude get

    init {
        addControllers(ButtonController(this, GATEWAY_LEFT_BUTTON_CONTROLLER, transport),
                ButtonController(this, GATEWAY_RIGHT_BUTTON_CONTROLLER, transport))
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            if (!o.isNull(STATUS_CHANNEL_0)) {
                statusLeft = o.getString(STATUS_CHANNEL_0)
                getControllerByType(GATEWAY_LEFT_BUTTON_CONTROLLER).state = statusLeft
            }

            if (!o.isNull(STATUS_CHANNEL_1)) {
                statusRight = o.getString(STATUS_CHANNEL_1)
                getControllerByType(GATEWAY_RIGHT_BUTTON_CONTROLLER).state = statusRight
            }

            super.parseData(json)
        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }

    override fun toString(): String {
        return super.toString() + "\nstatus left: $statusLeft, status right: $statusRight"
    }
}