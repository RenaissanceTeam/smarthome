package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.CRON_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class CronController(device: Device) : Controller(device, CRON_CONTROLLER_TYPE), Readable, Writable {

    override fun read(): String {
        return super.controllerRead(Property.DELAY_OFF)
    }


    override fun write(vararg params: Any): Result {
        setNewState((params[0] as Int).toString())
        return super.controllerWrite("cron_add", 0, params[0])
    }
}