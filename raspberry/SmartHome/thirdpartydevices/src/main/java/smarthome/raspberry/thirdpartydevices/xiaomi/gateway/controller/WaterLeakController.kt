package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_WATER_LEAK_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Device

class WaterLeakController (device: Device) : Controller(device, GATEWAY_WATER_LEAK_CONTROLLER), Readable {

    override fun read(): String {
        return controllerRead()
    }
}