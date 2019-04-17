package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device

import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.Expose
import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.*
import smarthome.library.common.constants.DeviceTypes.GATEWAY_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.*
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.GatewayLightOnOffController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.IlluminationController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.RGBController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport

class Gateway(sid: String, transport: UdpTransport) : GatewayDevice(sid, GATEWAY_TYPE) {

    @Exclude
    val transport: UdpTransport = transport
        @Exclude get

    @Exclude @Expose var ip: String = ""
        @Exclude get
    @Exclude @Expose var rgb: Long = 0
        @Exclude get
    @Exclude @Expose var illumination: Int = 0
        @Exclude get
    @Expose private var protoVersion: String = IDLE_STATUS

    init {
        addControllers(RGBController(this, transport),
                IlluminationController(this, transport),
                GatewayLightOnOffController(this, transport))
    }

    private fun configureIp(ip: String) {
        this.ip = ip
        transport.setGatewayIp(ip)
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            var light = false

            if (!o.isNull(IP_KEY))
                configureIp(o.getString(IP_KEY))

            if (!o.isNull(RGB_KEY)) {
                rgb = o.getLong(RGB_KEY)
                getControllerByType(GATEWAY_RGB_CONTROLLER).state = rgb.toString()

                if(rgb > 0)
                    light = true
            }

            if (!o.isNull(ILLUMINATION_KEY)) {
                illumination = o.getInt(ILLUMINATION_KEY)
                getControllerByType(GATEWAY_ILLUMINATION_CONTROLLER).state = illumination.toString()

                if(illumination > 0)
                    light = true
            }

            if (!o.isNull(PROTO_VERSION_KEY))
                protoVersion = o.getString(PROTO_VERSION_KEY)

            var lightStatus = STATUS_OFF
            if (light) lightStatus = STATUS_ON

            getControllerByType(GATEWAY_LIGHT_ON_OFF_CONTROLLER).state = lightStatus

            super.parseData(json)
        } catch (e: JSONException) {
            reportDataParseError(e)
        }

    }

    override fun toString(): String {
        return super.toString() + "\nip: $ip, rgb: $rgb, illumination: $illumination, protoVersion: $protoVersion"
    }

}