package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_WATER_LEAK_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice

class WaterLeakController (device: GatewayDevice) : Controller(device, GATEWAY_WATER_LEAK_CONTROLLER), GatewayReadable {

    override fun read(): String {
        return controllerRead()
    }
}