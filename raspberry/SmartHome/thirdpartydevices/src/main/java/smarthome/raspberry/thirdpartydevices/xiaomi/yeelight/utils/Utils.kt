package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder

class Utils {
    companion object {
        val GSON: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

        fun <T> mapJson(json: String, c: Class<T>): T {
            return GSON.fromJson(json, c)
        }
    }
}