package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.DEFAULT_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class DefaultController (device: YeelightDevice) : Controller(device, DEFAULT_CONTROLLER_TYPE), YeelightWritable {

    override fun write(params: String): Result {
        return super.controllerWrite(DEFAULT_CONTROLLER_TYPE)
    }
}