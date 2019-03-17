package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.DEFAULT_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class DefaultController (device: Device) : Controller(device, DEFAULT_CONTROLLER_TYPE), Writable {

    override fun write(vararg params: Any): Result {
        return super.controllerWrite(DEFAULT_CONTROLLER_TYPE)
    }
}