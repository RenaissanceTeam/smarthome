package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_MOTION_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice

class MotionController(device: GatewayDevice) : Controller(device, GATEWAY_MOTION_CONTROLLER), Readable {

    override fun read(): String {
        return super.controllerRead()
    }
}