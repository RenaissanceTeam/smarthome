package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.POWER_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class PowerController(device: YeelightDevice) : Controller(device, POWER_CONTROLLER_TYPE), Readable, Writable {

    override fun read(): String {
        return super.controllerRead(Property.POWER)
    }

    /**
     * @param params should be "on" or "off" (String)
     */
    override fun write(vararg params: Any): Result {
        val power: String = params[0] as String
        setNewState(power)
        return super.controllerWrite(POWER_CONTROLLER_TYPE, power, device.effect.effect, device.duration)
    }
}