package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.RGB_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.utils.Utils
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.utils.Utils.Companion.calculateRGB

class RGBController(device: Device) : Controller(device, RGB_CONTROLLER_TYPE), Readable, Writable {

    override fun read(): String {
        return super.controllerRead(Property.RGB)
    }

    /**
     * @param params {r}, {g}, {b} (ints from 0 to 255)
     */
    override fun write(vararg params: Any): Result {
        val r: Int = Utils.adjust(params[0] as Int, 0, 255)
        val g: Int = Utils.adjust(params[1] as Int, 0, 255)
        val b: Int = Utils.adjust(params[2] as Int, 0, 255)
        val rgb = calculateRGB(r, g, b)
        setNewState(rgb.toString())
        return super.controllerWrite(RGB_CONTROLLER_TYPE, rgb, device.effect.effect, device.duration)
    }
}