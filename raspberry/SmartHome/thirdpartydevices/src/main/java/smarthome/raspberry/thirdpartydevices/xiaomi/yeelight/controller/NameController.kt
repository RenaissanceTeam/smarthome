package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.NAME_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class NameController(device: Device) : Controller(device, NAME_CONTROLLER_TYPE), Readable, Writable {
    override fun read(): String {
        return super.controllerRead(Property.NAME)
    }

    /**
     * @param params {name} (String)
     */
    override fun write(vararg params: Any): Result {
        val name: String = params[0] as String // TODO: should iotDevice be renamed too?
        return super.controllerWrite(NAME_CONTROLLER_TYPE, name)
    }
}