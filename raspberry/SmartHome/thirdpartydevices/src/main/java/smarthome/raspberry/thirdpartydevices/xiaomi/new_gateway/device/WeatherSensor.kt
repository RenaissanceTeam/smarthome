package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device

import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.WEATHER_SENSOR_TYPE
import smarthome.library.common.constants.GATEWAY_HUMIDITY_CONTROLLER
import smarthome.library.common.constants.GATEWAY_PRESSURE_CONTROLLER
import smarthome.library.common.constants.GATEWAY_TEMPERATURE_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.STATUS_HUMIDITY
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.STATUS_PRESSURE
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.STATUS_TEMPERATURE
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.HumidityController
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.PressureController
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.TemperatureController
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.VoltageController

class WeatherSensor(sid: String)
    : Device(sid, WEATHER_SENSOR_TYPE) {

    init {
        addControllers(TemperatureController(this),
                HumidityController(this),
                PressureController(this),
                VoltageController(this))
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            if (!o.isNull(STATUS_TEMPERATURE))
                getControllerByType(GATEWAY_TEMPERATURE_CONTROLLER).state = (o.getString(STATUS_TEMPERATURE).toFloat() / 100).toString() + "C"

            if (!o.isNull(STATUS_HUMIDITY))
                getControllerByType(GATEWAY_HUMIDITY_CONTROLLER).state = (o.getString(STATUS_HUMIDITY).toFloat() / 100).toString() + "%"

            if (!o.isNull(STATUS_PRESSURE))
                getControllerByType(GATEWAY_PRESSURE_CONTROLLER).state = (o.getString(STATUS_PRESSURE).toFloat() / 100).toString() + "kPa"

            setVoltage(o)

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