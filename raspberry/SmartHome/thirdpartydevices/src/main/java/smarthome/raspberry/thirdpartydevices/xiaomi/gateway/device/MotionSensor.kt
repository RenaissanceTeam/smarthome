package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device

import com.google.gson.annotations.Expose
import org.json.JSONException
import org.json.JSONObject
import smarthome.library.common.constants.DeviceTypes.MOTION_SENSOR_TYPE
import smarthome.library.common.constants.GATEWAY_MOTION_CONTROLLER
import smarthome.library.common.constants.STATUS_KEY
import smarthome.library.common.constants.STATUS_MOTION
import smarthome.library.common.constants.STATUS_NO_MOTION
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.MotionController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.VoltageController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners.StateChangeListener
import java.time.LocalDateTime

class MotionSensor(sid: String, gatewaySid: String, stateChangeListener: StateChangeListener)
    : GatewayDevice(sid, MOTION_SENSOR_TYPE, gatewaySid, stateChangeListener) {

    @Expose private var lastMotion: LocalDateTime = LocalDateTime.now()

    init {
        addControllers(MotionController(this),
                VoltageController(this))
    }

    override fun parseData(json: String) {
        try {
            val o = JSONObject(json)

            val motionController = getControllerByType(GATEWAY_MOTION_CONTROLLER)

            if (!o.isNull(STATUS_KEY)) {
                motionController.state = o.getString(STATUS_KEY)

                if (motionController.state == STATUS_MOTION) {
                    lastMotion = LocalDateTime.now()
                    motionController.state += " $lastMotion"
                    invokeStateListener(motionController.state, this, motionController)
                }
            }

            if(!o.isNull(STATUS_NO_MOTION))
                motionController.state = "$STATUS_NO_MOTION  " + o.getInt(STATUS_NO_MOTION)

            setVoltage(o)

            super.parseData(json)
        } catch (e: JSONException) {
            reportDataParseError(e)
        }
    }

    override fun toString(): String {
        return super.toString() + "state: " + getControllerByType(GATEWAY_MOTION_CONTROLLER).state
    }
}