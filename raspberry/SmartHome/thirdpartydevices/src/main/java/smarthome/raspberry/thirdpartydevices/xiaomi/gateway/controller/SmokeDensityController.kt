package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_SMOKE_DENSITY_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice

class SmokeDensityController (device: GatewayDevice) : Controller(device, GATEWAY_SMOKE_DENSITY_CONTROLLER), Readable {

    override fun read(): String {
        return controllerRead()
    }
}