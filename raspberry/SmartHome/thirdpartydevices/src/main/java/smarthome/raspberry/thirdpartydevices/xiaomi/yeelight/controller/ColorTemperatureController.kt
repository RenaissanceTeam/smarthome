package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.COLOR_TEMPERATURE_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result
import smarthome.raspberry.thirdpartydevices.utils.Utils.Companion.adjust

class ColorTemperatureController(device: YeelightDevice) : Controller(device, COLOR_TEMPERATURE_CONTROLLER_TYPE), YeelightReadable, YeelightWritable {

    /**
     * @param params {color_temperature} (int from 1700 to 6500)
     */
    override fun write(params: String): Result {
        val colorTemp: Int = adjust(params.toInt(), 1700, 6500)
        return super.controllerWrite(COLOR_TEMPERATURE_CONTROLLER_TYPE, colorTemp, device.effect.effect, device.duration)
    }

    override fun read(): String {
        return super.controllerRead(Property.COLOR_TEMPERATURE)
    }
}