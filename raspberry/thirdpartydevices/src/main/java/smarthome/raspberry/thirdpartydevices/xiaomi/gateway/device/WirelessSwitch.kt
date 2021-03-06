package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device

import com.google.firebase.firestore.Exclude
import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.WIRELESS_SWITCH_TYPE
import smarthome.library.common.constants.GATEWAY_BUTTON_CLICK_CONTROLLER
import smarthome.library.common.constants.STATUS_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.interfaces.TransportSettable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.ButtonClickController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport

class WirelessSwitch(sid: String, @Exclude private var transport: UdpTransport, gatewaySid: String)
    : GatewayDevice(sid, WIRELESS_SWITCH_TYPE, gatewaySid), TransportSettable {

    init {
        addControllers(ButtonClickController(this, transport))
    }

    override fun setUpTransport(transport: UdpTransport) {
        this.transport = transport
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            if (!o.isNull(STATUS_KEY))
                getControllerByType(GATEWAY_BUTTON_CLICK_CONTROLLER).state = o.getString(STATUS_KEY)

            setVoltage(o)

            super.parseData(json)
        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }

    override fun toString(): String {
        return super.toString() + "\nstatus: " + getControllerByType("left").state
    }
}