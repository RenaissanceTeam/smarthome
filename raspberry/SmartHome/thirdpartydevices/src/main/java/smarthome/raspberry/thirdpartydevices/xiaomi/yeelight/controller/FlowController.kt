package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.FLOW_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result
import java.lang.Exception

class FlowController(device: YeelightDevice) : Controller(device, FLOW_CONTROLLER_TYPE), YeelightWritable {

    override fun write(params: String): Result {
        throw Exception("not implemented")
    }

}