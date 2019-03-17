package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.TOGGLE_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class ToggleController(device: Device) : Controller(device, TOGGLE_CONTROLLER_TYPE), Readable, Writable {

    override fun read(): String {
        return super.controllerRead(Property.POWER)
    }

    override fun write(vararg params: Any): Result {
        return super.controllerWrite(TOGGLE_CONTROLLER_TYPE)
    }
}