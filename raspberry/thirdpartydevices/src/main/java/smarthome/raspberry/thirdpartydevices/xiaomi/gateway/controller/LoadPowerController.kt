package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_LOAD_POWER_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice

class LoadPowerController (device: GatewayDevice) : Controller(device, GATEWAY_LOAD_POWER_CONTROLLER), GatewayReadable {

    override fun read(): String {
        return controllerRead()
    }
}