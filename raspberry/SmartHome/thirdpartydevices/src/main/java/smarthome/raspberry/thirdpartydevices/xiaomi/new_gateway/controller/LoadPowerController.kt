package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller

import smarthome.library.common.constants.GATEWAY_LOAD_POWER_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device.Device

class LoadPowerController (device: Device) : Controller(device, GATEWAY_LOAD_POWER_CONTROLLER), Readable {

    override fun read(): String {
        return controllerRead()
    }
}