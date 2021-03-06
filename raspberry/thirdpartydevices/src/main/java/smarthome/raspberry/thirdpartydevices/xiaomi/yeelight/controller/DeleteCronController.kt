package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.library.common.constants.DELETE_CRON_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.READ_NOT_SUPPORTED_MESSAGE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class DeleteCronController(device: YeelightDevice) : Controller(device, DELETE_CRON_CONTROLLER_TYPE), YeelightWritable, YeelightReadable {

    override fun write(params: String): Result {
        return super.controllerWrite(type, 0)
    }

    override fun read(): String {
        return READ_NOT_SUPPORTED_MESSAGE
    }
}