package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device

import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.DOOR_WINDOW_SENSOR_TYPE
import smarthome.library.common.constants.GATEWAY_DOOR_WINDOW_CONTROLLER
import smarthome.library.common.constants.STATUS_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.DoorWindowController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.VoltageController

class DoorWindowSensor(sid: String, gatewaySid: String) : GatewayDevice(sid, DOOR_WINDOW_SENSOR_TYPE, gatewaySid) {

    init {
        addControllers(DoorWindowController(this),
                VoltageController(this))
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            if (!o.isNull(STATUS_KEY))
                getControllerByType(GATEWAY_DOOR_WINDOW_CONTROLLER).state = o.getString(STATUS_KEY)

            setVoltage(o)

            super.parseData(json)
        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }
}