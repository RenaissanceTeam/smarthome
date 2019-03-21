package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_POWER_CONSUMED_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice

class PowerConsumedController (device: GatewayDevice) : Controller(device, GATEWAY_POWER_CONSUMED_CONTROLLER), Readable {

    override fun read(): String {
        return controllerRead()
    }
}