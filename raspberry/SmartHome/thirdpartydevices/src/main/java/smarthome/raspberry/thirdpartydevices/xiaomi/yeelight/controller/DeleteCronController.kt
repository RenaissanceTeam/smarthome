package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.DELETE_CRON_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class DeleteCronController(device: Device) : Controller(device, DELETE_CRON_CONTROLLER_TYPE), Writable {

    override fun write(vararg params: Any): Result {
        return super.controllerWrite(DELETE_CRON_CONTROLLER_TYPE, 0)
    }
}