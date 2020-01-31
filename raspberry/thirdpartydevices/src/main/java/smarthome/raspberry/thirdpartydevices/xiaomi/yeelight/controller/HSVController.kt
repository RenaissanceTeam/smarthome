package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.library.common.constants.HSV_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.utils.Utils.adjust
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class HSVController(device: YeelightDevice) : Controller(device, HSV_CONTROLLER_TYPE), YeelightReadable, YeelightWritable {

    override fun read(): String {
        val res = super.controllerRead(Property.HUE, Property.SAT)
        setNewState(res[0] + " " + res[1])
        return res.joinToString(" ")
    }

    /**
     * @param params {hue} (int from 0 to 359), {saturation} (int from 0 to 100)
     */
    override fun write(params: String): Result {
        val options = params.split(" ")
        val hue: Int = adjust(options[0].toInt(), 0, 359)
        val saturation: Int = adjust(options[1].toInt(), 0, 100)
        return super.controllerWrite(type, hue, saturation, device.effect.effect, device.duration)
    }
}