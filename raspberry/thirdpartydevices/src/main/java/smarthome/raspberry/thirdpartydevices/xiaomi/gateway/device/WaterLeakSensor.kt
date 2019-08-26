package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device

import com.google.gson.annotations.Expose
import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.WATER_LEAK_SENSOR_TYPE
import smarthome.library.common.constants.GATEWAY_WATER_LEAK_CONTROLLER
import smarthome.library.common.constants.STATUS_KEY
import smarthome.library.common.constants.STATUS_WATER_LEAK
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.VoltageController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.WaterLeakController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners.WaterLeakListener

class WaterLeakSensor(sid: String,
                      gatewaySid: String,
                      waterLeakListener: WaterLeakListener? = null)
    : GatewayDevice(sid, WATER_LEAK_SENSOR_TYPE, gatewaySid, waterLeakListener = waterLeakListener) {

    @Expose private var leak: Boolean = false

    init {
        addControllers(WaterLeakController(this),
                VoltageController(this))
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            if (!o.isNull(STATUS_KEY)) {
                val waterLeakController = getControllerByType(GATEWAY_WATER_LEAK_CONTROLLER)
                waterLeakController.state = o.getString(STATUS_KEY)

                val leakStatus = waterLeakController.state == STATUS_WATER_LEAK

                if(leak != leakStatus) {
                    waterLeakListener?.onWaterLeakStatusChanged(leakStatus, this, waterLeakController)
                    leak = leakStatus
                }
            }

            setVoltage(o)

            super.parseData(json)
        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }
}