package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device

import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.WIRED_DUAL_WALL_SWITCH_TYPE
import smarthome.library.common.constants.GATEWAY_LEFT_BUTTON_CONTROLLER
import smarthome.library.common.constants.GATEWAY_RIGHT_BUTTON_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.IDLE_STATUS
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.STATUS_CHANNEL_0
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.STATUS_CHANNEL_1
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.ButtonController

class WiredDualWallSwitch (sid: String,
                           var statusLeft: String = IDLE_STATUS,
                           var statusRight: String = IDLE_STATUS)
    : Device(sid, WIRED_DUAL_WALL_SWITCH_TYPE) {

    init {
        addControllers(ButtonController(this, GATEWAY_LEFT_BUTTON_CONTROLLER),
                ButtonController(this, GATEWAY_RIGHT_BUTTON_CONTROLLER))
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

        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }

    override fun toString(): String {
        return super.toString() + "\nstatus left: $statusLeft, status right: $statusRight"
    }
}