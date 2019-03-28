package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result

import com.google.gson.annotations.Expose
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.exception.ResultErrorException

class Result {
    @Expose
    val id: Int? = null

    @Expose
    val result: Array<String>? = null

    @Expose
    val errorResult: ErrorResult? = null

    fun isOkResult(): Boolean {
        return errorResult == null
    }

    fun getException(): ResultErrorException? {
        return errorResult?.let { ResultErrorException(it.code!!, it.message!!) }
    }
}