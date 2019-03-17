package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.exception

import java.lang.Exception

class ResultErrorException(val code: Int, message: String) : Exception("$message($code)")