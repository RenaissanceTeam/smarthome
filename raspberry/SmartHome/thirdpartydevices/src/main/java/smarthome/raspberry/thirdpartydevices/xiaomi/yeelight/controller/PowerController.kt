package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.POWER_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class PowerController(device: YeelightDevice) : Controller(device, POWER_CONTROLLER_TYPE), YeelightReadable, YeelightWritable {

    override fun read(): String {
        return super.controllerRead(Property.POWER)
    }

    /**
     * @param params should be "on" or "off" (String)
     */
    override fun write(params: String): Result {
        return super.controllerWrite(POWER_CONTROLLER_TYPE, params, device.effect.effect, device.duration)
    }
}