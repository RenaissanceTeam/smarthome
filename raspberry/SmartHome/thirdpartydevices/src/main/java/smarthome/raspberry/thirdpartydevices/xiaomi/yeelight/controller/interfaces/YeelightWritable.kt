package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

interface YeelightWritable {
    fun write(params: String): Result
}