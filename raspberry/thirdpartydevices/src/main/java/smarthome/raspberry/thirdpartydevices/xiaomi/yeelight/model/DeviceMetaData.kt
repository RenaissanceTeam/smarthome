package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.model

import com.google.gson.annotations.Expose

data class DeviceMetaData(@Expose val model: String,
                          @Expose val firmware: String,
                          @Expose val support: List<String>) {
    override fun toString(): String {
        return "--- Yeelight device metadata ---\n" +
                "model: $model \n" +
                "firmware_version: $firmware \n" +
                "supported_services: ${support.joinToString(", ")}"
    }
}