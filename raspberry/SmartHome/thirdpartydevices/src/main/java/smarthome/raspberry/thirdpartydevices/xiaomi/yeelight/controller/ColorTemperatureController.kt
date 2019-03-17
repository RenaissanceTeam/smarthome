package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.utils.Utils.Companion.adjust

class ColorTemperatureController(device: Device) : Controller(device, Property.COLOR_TEMPERATURE.property), Readable, Writable {

    /**
     * @param params {color_temperature} (int from 1700 to 6500)
     */
    override fun write(vararg params: Any): Result {
        val colorTemp: Int = adjust(params[0] as Int, 1700, 6500)
        return super.controllerWrite("set_ct_abx", colorTemp, device.effect.effect, device.duration)
    }

    override fun read(): String {
        return super.controllerRead(Property.COLOR_TEMPERATURE)
    }
}