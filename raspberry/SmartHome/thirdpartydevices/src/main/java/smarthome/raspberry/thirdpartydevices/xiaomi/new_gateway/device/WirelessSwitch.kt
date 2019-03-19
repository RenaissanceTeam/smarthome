package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device

import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.WIRELESS_SWITCH_TYPE
import smarthome.library.common.constants.GATEWAY_BUTTON_CLICK_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.STATUS_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.ButtonClickController

class WirelessSwitch(sid: String)
    : Device(sid, WIRELESS_SWITCH_TYPE) {

    init {
        addControllers(ButtonClickController(this))
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            if (!o.isNull(STATUS_KEY))
                getControllerByType(GATEWAY_BUTTON_CLICK_CONTROLLER).state = o.getString(STATUS_KEY)

            setVoltage(o)

        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }

    override fun toString(): String {
        return super.toString() + "\nstatus: " + getControllerByType("left").state
    }
}