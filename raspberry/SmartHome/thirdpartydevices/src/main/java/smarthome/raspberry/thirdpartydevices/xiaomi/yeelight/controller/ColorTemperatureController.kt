package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class ColorTemperatureController(device: Device) : Controller(device, Property.COLOR_TEMPERATURE.property), Readable, Writable {

    override fun write(vararg params: Any): Result {
        return super.controllerWrite("set_ct_abx", *params)
    }

    override fun read(property: Property): String {
        return super.controllerRead(property)
    }
}