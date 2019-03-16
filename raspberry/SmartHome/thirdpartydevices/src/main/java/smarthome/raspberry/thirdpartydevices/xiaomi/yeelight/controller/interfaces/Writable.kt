package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

interface Writable {
    fun write(vararg params: Any): Result
}