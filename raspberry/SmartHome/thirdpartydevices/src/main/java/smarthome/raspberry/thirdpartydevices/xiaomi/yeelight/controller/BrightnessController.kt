package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.utils.Utils.Companion.adjust

class BrightnessController(device: Device) : Controller(device, Property.BRIGHTNESS.property), Readable, Writable {

    override fun read(): String {
        return super.controllerRead(Property.BRIGHTNESS)
    }

    override fun write(vararg params: Any): Result {
        val brightness: Int = adjust(params[0] as Int, 1, 100)
        setNewState(brightness.toString())
        return super.controllerWrite("set_bright", brightness, device.effect.effect, device.duration)
    }
}