package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.library.common.constants.RGB_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.utils.Utils.adjust
import smarthome.raspberry.thirdpartydevices.utils.Utils.calculateRGB
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class RGBController(device: YeelightDevice) : Controller(device, RGB_CONTROLLER_TYPE), YeelightReadable, YeelightWritable {

    override fun read(): String {
        return super.controllerRead(Property.RGB)
    }

    /**
     * @param params rgb (int)
     */
    override fun write(params: String): Result {
        val args = params.split(" ")
        if(args.size > 1) {
            val r = adjust(args[0].toInt(), 0, 255)
            val g = adjust(args[1].toInt(), 0, 255)
            val b = adjust(args[2].toInt(), 0, 255)
            val rgb = calculateRGB(r, g, b)
            return super.controllerWrite(type, rgb, device.effect.effect, device.duration)
        }
        return super.controllerWrite(type, params.toInt(), device.effect.effect, device.duration)
    }
}