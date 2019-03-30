package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.CRON_ADD_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class CronController(device: YeelightDevice) : Controller(device, CRON_ADD_CONTROLLER_TYPE), YeelightReadable, YeelightWritable {

    override fun read(): String {
        return super.controllerRead(Property.DELAY_OFF)
    }


    /**
     * @param params {delay} (int)
     */
    override fun write(params: String): Result {
        return super.controllerWrite(CRON_ADD_CONTROLLER_TYPE, 0, params.toInt())
    }
}