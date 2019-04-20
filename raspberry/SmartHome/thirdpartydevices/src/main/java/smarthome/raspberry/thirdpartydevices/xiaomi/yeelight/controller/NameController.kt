package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.library.common.constants.NAME_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class NameController(device: YeelightDevice) : Controller(device, NAME_CONTROLLER_TYPE), YeelightReadable, YeelightWritable {
    override fun read(): String {
        return super.controllerRead(Property.NAME)
    }

    /**
     * @param params {name} (String)
     */
    override fun write(params: String): Result {
        return super.controllerWrite(type, params) // TODO: should iotDevice be renamed too?
    }
}