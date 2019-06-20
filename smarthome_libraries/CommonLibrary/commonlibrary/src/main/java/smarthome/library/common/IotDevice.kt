package smarthome.library.common

import com.google.gson.Gson

class IotDevice(val name: String,
                val description: String,
                val guid: Long,
                val controllers: List<BaseController>
                ) {

    /**
     * Serialize to json using Gson, the class later can be deserialized using Gson
     */
    fun gsonned(): String {
        return Gson().toJson(this)
    }

    override fun hashCode(): Int {
        return guid.toInt()
    }


    override fun equals(obj: Any?): Boolean {
        return (obj as? IotDevice)?.guid == guid
    }
}
