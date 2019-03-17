package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.model

data class DeviceMetaData(val model: String,
                          val firmware: String,
                          val support: List<String>) {
    override fun toString(): String {
        return "--- Yeelight device metadata ---\n" +
                "model: $model \n" +
                "firmware_version: $firmware \n" +
                "supported_services: ${support.joinToString(", ")}"
    }
}