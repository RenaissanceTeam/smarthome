package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_TIME_IN_USE_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice

class TimeInUseController(device: GatewayDevice) : Controller(device, GATEWAY_TIME_IN_USE_CONTROLLER), GatewayReadable {

    override fun read(): String {
        return controllerRead()
    }
}