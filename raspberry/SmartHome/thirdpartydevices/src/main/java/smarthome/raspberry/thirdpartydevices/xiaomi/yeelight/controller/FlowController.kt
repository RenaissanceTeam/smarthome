package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.FLOW_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result
import java.lang.Exception

class FlowController(device: YeelightDevice) : Controller(device, FLOW_CONTROLLER_TYPE), Writable {

    override fun write(vararg params: Any): Result {
        throw Exception("not implemented")
    }

}