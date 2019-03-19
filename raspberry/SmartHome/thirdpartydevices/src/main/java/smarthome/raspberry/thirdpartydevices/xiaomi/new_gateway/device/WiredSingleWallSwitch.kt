package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device

import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.WIRED_SINGLE_WALL_SWITCH_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.STATUS_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.ButtonController

class WiredSingleWallSwitch(sid: String)
    : Device(sid, WIRED_SINGLE_WALL_SWITCH_TYPE) {

    init {
        addControllers(ButtonController(this, "left"))
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            if (!o.isNull(STATUS_KEY))
                getControllerByType("left").state = o.getString(STATUS_KEY)


        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }

    override fun toString(): String {
        return super.toString() + "\nstatus: " + getControllerByType("left").state
    }
}