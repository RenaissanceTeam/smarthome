package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway

import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.GATEWAY_TYPE
import smarthome.library.common.constants.GATEWAY_ILLUMINATION_CONTROLLER
import smarthome.library.common.constants.GATEWAY_RGB_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.*
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.IlluminationController
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.RGBController
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.utils.UdpTransport

class Gateway(sid: String, var ip: String, val transport: UdpTransport) : Device(sid, GATEWAY_TYPE) {

    var rgb: Long = 0
    var illumination: Int = 0
    private var protoVersion: String = IDLE_STATUS

    init {
        addControllers(RGBController(this, transport),
                IlluminationController(this, transport))
    }

    private fun configureIp(ip: String) {
        this.ip = ip
        transport.setGatewayIp(ip)
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            if (!o.isNull(IP_KEY))
                configureIp(o.getString(IP_KEY))

            if (!o.isNull(RGB_KEY)) {
                rgb = o.getLong(RGB_KEY)
                getControllerByType(GATEWAY_RGB_CONTROLLER).state = rgb.toString()
            }

            if (!o.isNull(ILLUMINATION_KEY)) {
                illumination = o.getInt(ILLUMINATION_KEY)
                getControllerByType(GATEWAY_ILLUMINATION_CONTROLLER).state = illumination.toString()
            }

            if (!o.isNull(PROTO_VERSION_KEY))
                protoVersion = o.getString(PROTO_VERSION_KEY)

        } catch (e: JSONException) {
            reportDataParseError(e)
        }

    }

    override fun toString(): String {
        return super.toString() + "\nrgb: $rgb, illumination: $illumination, protoVersion: $protoVersion"
    }

}