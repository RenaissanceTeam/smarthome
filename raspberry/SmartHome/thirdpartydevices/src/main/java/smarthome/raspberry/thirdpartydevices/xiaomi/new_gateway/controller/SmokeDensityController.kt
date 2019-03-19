package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller

import smarthome.library.common.constants.GATEWAY_SMOKE_DENSITY_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device.Device

class SmokeDensityController (device: Device) : Controller(device, GATEWAY_SMOKE_DENSITY_CONTROLLER), Readable {

    override fun read(): String {
        return controllerRead()
    }
}