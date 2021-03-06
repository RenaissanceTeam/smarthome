package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_VOLTAGE_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice

class VoltageController(device: GatewayDevice) : Controller(device, GATEWAY_VOLTAGE_CONTROLLER), GatewayReadable {

    override fun read(): String {
        return controllerRead()
    }

}