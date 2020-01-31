package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device

import com.google.firebase.firestore.Exclude
import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.WIRED_SINGLE_WALL_SWITCH_TYPE
import smarthome.library.common.constants.GATEWAY_SINGLE_BUTTON_CONTROLLER
import smarthome.library.common.constants.STATUS_KEY
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.interfaces.TransportSettable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.ButtonController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport

class WiredSingleWallSwitch(sid: String, @Exclude private var transport: UdpTransport, gatewaySid: String)
    : GatewayDevice(sid, WIRED_SINGLE_WALL_SWITCH_TYPE, gatewaySid), TransportSettable {

    init {
        addControllers(ButtonController(this, GATEWAY_SINGLE_BUTTON_CONTROLLER, transport))
    }

    override fun setUpTransport(transport: UdpTransport) {
        this.transport = transport

        for (controller in controllers)
            (controller as TransportSettable).setUpTransport(transport)
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            if (!o.isNull(STATUS_KEY))
                getControllerByType("left").state = o.getString(STATUS_KEY)

            super.parseData(json)
        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }

    override fun toString(): String {
        return super.toString() + "\nstatus: " + getControllerByType("left").state
    }
}