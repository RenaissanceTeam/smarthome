package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_MOTION_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice

class MotionController(device: GatewayDevice) : Controller(device, GATEWAY_MOTION_CONTROLLER), GatewayReadable {

    override fun read(): String {
        return super.controllerRead()
    }
}