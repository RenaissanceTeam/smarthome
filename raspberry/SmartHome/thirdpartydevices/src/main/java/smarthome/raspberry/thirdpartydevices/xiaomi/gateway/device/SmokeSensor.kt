package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device

import com.google.gson.annotations.Expose
import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.SMOKE_SENSOR_TYPE
import smarthome.library.common.constants.GATEWAY_SMOKE_ALARM_CONTROLLER
import smarthome.library.common.constants.GATEWAY_SMOKE_DENSITY_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.ALARM_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.DENSITY_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.SmokeAlarmController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.SmokeDensityController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.VoltageController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners.SmokeAlarmListener

class SmokeSensor(sid: String,
                  gatewaySid: String,
                  smokeAlarmListener: SmokeAlarmListener? = null)
    : GatewayDevice(sid, SMOKE_SENSOR_TYPE, gatewaySid, smokeAlarmListener = smokeAlarmListener) {

    @Expose
    private var alarm: Boolean = false

    init {
        addControllers(SmokeAlarmController(this),
                SmokeDensityController(this),
                VoltageController(this))
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            if (!o.isNull(ALARM_KEY)) {
                val smokeAlarmController = getControllerByType(GATEWAY_SMOKE_ALARM_CONTROLLER)
                val alarmStatus = o.getString(ALARM_KEY) == "1"

                if (alarm != alarmStatus)
                    smokeAlarmListener?.onSmokeAlarmStatusChanged(alarmStatus, this, smokeAlarmController)

                alarm = alarmStatus

                smokeAlarmController.state = alarm.toString()
            }

            if (!o.isNull(DENSITY_KEY))
                getControllerByType(GATEWAY_SMOKE_DENSITY_CONTROLLER).state =
                        (o.getString(DENSITY_KEY).toFloat() / 100).toString()

            setVoltage(o)

            super.parseData(json)
        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }

    override fun toString(): String {
        return super.toString() + "\nalarm: $alarm, density: " + getControllerByType(GATEWAY_SMOKE_DENSITY_CONTROLLER).state
    }
}