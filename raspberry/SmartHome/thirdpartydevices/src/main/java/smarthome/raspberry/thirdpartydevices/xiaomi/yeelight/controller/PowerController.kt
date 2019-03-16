package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class PowerController(device: Device) : Controller(device, Property.POWER.property), Readable, Writable {

    override fun read(): String {
        return super.controllerRead(Property.POWER)
    }

    /**
     * @param params should be "on" or "off"
     */
    override fun write(vararg params: Any): Result {
        val power: String = params[0] as String
        setNewState(power)
        return super.controllerWrite("set_power", power, device.effect.effect, device.duration)
    }
}