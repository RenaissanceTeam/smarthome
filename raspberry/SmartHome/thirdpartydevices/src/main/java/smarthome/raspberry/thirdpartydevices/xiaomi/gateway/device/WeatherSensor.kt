package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device

import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.WEATHER_SENSOR_TYPE
import smarthome.library.common.constants.GATEWAY_HUMIDITY_CONTROLLER
import smarthome.library.common.constants.GATEWAY_PRESSURE_CONTROLLER
import smarthome.library.common.constants.GATEWAY_TEMPERATURE_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.STATUS_PRESSURE
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.PressureController

class WeatherSensor(sid: String, gatewaySid: String)
    : THSensor(sid, WEATHER_SENSOR_TYPE, gatewaySid) {

    init {
        addControllers(PressureController(this))
    }

    override fun parseData(json: String) {
        super.parseData(json)
        try {
            val o = JSONObject(json)

            if (!o.isNull(STATUS_PRESSURE))
                getControllerByType(GATEWAY_PRESSURE_CONTROLLER).state = (o.getString(STATUS_PRESSURE).toFloat() / 1000).toString() + "kPa"

            super.parseData(json)
        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }

    override fun toString(): String {
        return super.toString() + "\ntemperature: " + getControllerByType(GATEWAY_TEMPERATURE_CONTROLLER).state +
                ", humidity: " + getControllerByType(GATEWAY_HUMIDITY_CONTROLLER).state +
                ", pressure: " + getControllerByType(GATEWAY_PRESSURE_CONTROLLER).state
    }
}