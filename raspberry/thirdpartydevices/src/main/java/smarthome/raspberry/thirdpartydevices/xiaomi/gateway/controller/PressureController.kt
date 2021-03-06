package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_PRESSURE_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice

class PressureController(device: GatewayDevice) : Controller(device, GATEWAY_PRESSURE_CONTROLLER), GatewayReadable {

    override fun read(): String {
        return controllerRead()
    }
}