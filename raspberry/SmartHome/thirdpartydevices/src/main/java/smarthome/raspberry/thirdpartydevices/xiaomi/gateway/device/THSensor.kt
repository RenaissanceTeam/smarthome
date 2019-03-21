package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device

import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.TEMPERATURE_HUMIDITY_SENSOR_TYPE
import smarthome.library.common.constants.GATEWAY_HUMIDITY_CONTROLLER
import smarthome.library.common.constants.GATEWAY_TEMPERATURE_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.STATUS_HUMIDITY
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.STATUS_TEMPERATURE
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.HumidityController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.TemperatureController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.VoltageController

open class THSensor (sid: String,
                     type: String = TEMPERATURE_HUMIDITY_SENSOR_TYPE,
                     gatewaySid: String)
    : GatewayDevice(sid, type, gatewaySid) {

    init {
        addControllers(TemperatureController(this),
                HumidityController(this),
                VoltageController(this))
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            if (!o.isNull(STATUS_TEMPERATURE))
                getControllerByType(GATEWAY_TEMPERATURE_CONTROLLER).state = (o.getString(STATUS_TEMPERATURE).toFloat() / 100).toString() + "C"

            if (!o.isNull(STATUS_HUMIDITY))
                getControllerByType(GATEWAY_HUMIDITY_CONTROLLER).state = (o.getString(STATUS_HUMIDITY).toFloat() / 100).toString() + "%"

            setVoltage(o)

        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }

    override fun toString(): String {
        return super.toString() + "\ntemperature: " + getControllerByType(GATEWAY_TEMPERATURE_CONTROLLER).state +
                ", humidity: " + getControllerByType(GATEWAY_HUMIDITY_CONTROLLER).state
    }
}