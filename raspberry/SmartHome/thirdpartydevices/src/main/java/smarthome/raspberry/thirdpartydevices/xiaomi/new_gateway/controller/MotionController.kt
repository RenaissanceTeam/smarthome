package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller

import smarthome.library.common.constants.GATEWAY_MOTION_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device.Device

class MotionController(device: Device) : Controller(device, GATEWAY_MOTION_CONTROLLER), Readable {

    override fun read(): String {
        return super.controllerRead()
    }
}