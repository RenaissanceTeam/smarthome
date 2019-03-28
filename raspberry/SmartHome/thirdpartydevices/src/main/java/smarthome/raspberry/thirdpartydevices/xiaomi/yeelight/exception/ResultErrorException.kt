package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.exception

class ResultErrorException(val code: Int, message: String) : Exception("$message($code)")