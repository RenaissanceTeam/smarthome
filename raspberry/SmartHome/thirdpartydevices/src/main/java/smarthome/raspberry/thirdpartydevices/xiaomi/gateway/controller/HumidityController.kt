package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_HUMIDITY_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice

class HumidityController(device: GatewayDevice) : Controller(device, GATEWAY_HUMIDITY_CONTROLLER), GatewayReadable {

    override fun read(): String {
        return controllerRead()
    }
}