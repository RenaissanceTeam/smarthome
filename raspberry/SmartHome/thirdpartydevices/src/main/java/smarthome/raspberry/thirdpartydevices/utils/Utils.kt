package smarthome.raspberry.thirdpartydevices.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlin.math.max
import kotlin.math.min

class Utils {
    companion object {
        val GSON: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val DEF_GSON: Gson = GsonBuilder().create()

        fun adjust(value: Int, min: Int, max: Int): Int {
            return max(min, min(max, value))
        }

        fun calculateRGB(r: Int, g: Int, b: Int): Int {
            return r * 65536 + g * 256 + b
        }

        fun <T> mapJsonExpose(json: String, c: Class<T>): T {
            return GSON.fromJson(json, c)
        }

        fun <T> mapJson(json: String, c: Class<T>): T {
            return DEF_GSON.fromJson(json, c)
        }
    }
}