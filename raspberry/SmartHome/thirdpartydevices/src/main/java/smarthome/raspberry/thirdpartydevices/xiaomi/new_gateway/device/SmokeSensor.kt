package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device

import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.SMOKE_SENSOR_TYPE
import smarthome.library.common.constants.GATEWAY_SMOKE_ALARM_CONTROLLER
import smarthome.library.common.constants.GATEWAY_SMOKE_DENSITY_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.ALARM_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.DENSITY_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.SmokeAlarmController
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.SmokeDensityController
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.VoltageController
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.listeners.SmokeAlarmListener

class SmokeSensor(sid: String,
                  private var alarm: Boolean = false,
                  smokeAlarmListener: SmokeAlarmListener? = null)
    : Device(sid, SMOKE_SENSOR_TYPE, smokeAlarmListener = smokeAlarmListener) {

    init {
        addControllers(SmokeAlarmController(this),
                SmokeDensityController(this),
                VoltageController(this))
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            if (!o.isNull(ALARM_KEY)) {
                val alarmStatus = o.getBoolean(ALARM_KEY)

                if (alarm != alarmStatus)
                    smokeAlarmListener?.onSmokeAlarmStatusChanged(alarmStatus)

                alarm = alarmStatus

                getControllerByType(GATEWAY_SMOKE_ALARM_CONTROLLER).state = alarm.toString()
            }

            if (!o.isNull(DENSITY_KEY))
                getControllerByType(GATEWAY_SMOKE_DENSITY_CONTROLLER).state =
                        (o.getString(DENSITY_KEY).toFloat() / 100).toString()

            setVoltage(o)

        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }

    override fun toString(): String {
        return super.toString() + "\nalarm: $alarm, density: " + getControllerByType(GATEWAY_SMOKE_DENSITY_CONTROLLER).state
    }
}