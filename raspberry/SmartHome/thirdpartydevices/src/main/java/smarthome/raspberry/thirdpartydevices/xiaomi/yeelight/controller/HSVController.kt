package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.HSV_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.utils.Utils.Companion.adjust

class HSVController(device: Device) : Controller(device, HSV_CONTROLLER_TYPE), Readable, Writable {

    override fun read(): String {
        val res = super.controllerRead(Property.HUE, Property.SAT)
        setNewState("hue: " + res[0] + " saturation: " + res[1])
        return res.joinToString(" ")
    }

    override fun write(vararg params: Any): Result {
        val hue: Int = adjust(params[0] as Int, 0, 359)
        val saturation: Int = adjust(params[1] as Int, 0, 100)
        setNewState("$hue $saturation")
        return super.controllerWrite("set_hsv", hue, saturation, device.effect.effect, device.duration)
    }
}