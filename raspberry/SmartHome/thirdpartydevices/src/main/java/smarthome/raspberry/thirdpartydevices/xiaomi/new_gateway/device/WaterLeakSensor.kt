package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device

import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.WATER_LEAK_SENSOR_TYPE
import smarthome.library.common.constants.GATEWAY_WATER_LEAK_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.STATUS_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.STATUS_WATER_LEAK
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.VoltageController
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.WaterLeakController
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.listeners.WaterLeakListener

class WaterLeakSensor(sid: String,
                      waterLeakListener: WaterLeakListener? = null)
    : Device(sid, WATER_LEAK_SENSOR_TYPE, waterLeakListener = waterLeakListener) {

    private var leak: Boolean = false

    init {
        addControllers(WaterLeakController(this),
                VoltageController(this))
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            if (!o.isNull(STATUS_KEY)) {
                getControllerByType(GATEWAY_WATER_LEAK_CONTROLLER).state = o.getString(STATUS_KEY)

                val leakStatus = getControllerByType(GATEWAY_WATER_LEAK_CONTROLLER).state == STATUS_WATER_LEAK

                if(leak != leakStatus) {
                    waterLeakListener?.onWaterLeakStatusChanged(leakStatus)
                    leak = leakStatus
                }
            }

            setVoltage(o)

        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }
}