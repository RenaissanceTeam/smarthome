package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.DELETE_CRON_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class DeleteCronController(device: YeelightDevice) : Controller(device, DELETE_CRON_CONTROLLER_TYPE), YeelightWritable {

    override fun write(params: String): Result {
        return super.controllerWrite(DELETE_CRON_CONTROLLER_TYPE, 0)
    }
}