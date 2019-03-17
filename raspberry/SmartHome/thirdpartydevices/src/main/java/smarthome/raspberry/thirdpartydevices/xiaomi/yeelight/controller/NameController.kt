package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class NameController(device: Device) : Controller(device, Property.NAME.property), Readable, Writable {
    override fun read(): String {
        return super.controllerRead(Property.NAME)
    }

    /**
     * @param params {name} (String)
     */
    override fun write(vararg params: Any): Result {
        val name: String = params[0] as String
        return super.controllerWrite("set_name", name)
    }
}