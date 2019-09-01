package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.library.common.constants.BRIGHTNESS_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.utils.Utils.adjust
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class BrightnessController(device: YeelightDevice) : Controller(device, BRIGHTNESS_CONTROLLER_TYPE), YeelightReadable, YeelightWritable {

    override fun read(): String {
        return super.controllerRead(Property.BRIGHTNESS)
    }

    /**
     * @param params {brightness} (int from 1 to 100)
     */
    override fun write(params: String): Result {
        val brightness: Int = adjust(params.toInt(), 1, 100)
        setNewState(brightness.toString())
        return super.controllerWrite(type, brightness, device.effect.effect, device.duration)
    }
}