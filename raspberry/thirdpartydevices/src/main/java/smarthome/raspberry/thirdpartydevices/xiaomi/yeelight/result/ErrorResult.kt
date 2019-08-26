package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result

import com.google.gson.annotations.Expose

data class ErrorResult(@Expose val code: Int? = null, @Expose val message: String? = null)