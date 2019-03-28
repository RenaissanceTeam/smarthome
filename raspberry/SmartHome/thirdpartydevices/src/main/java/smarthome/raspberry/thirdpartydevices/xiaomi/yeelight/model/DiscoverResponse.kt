package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.model

data class DiscoverResponse (var ip: String = "",
                             var port: Int = 0,
                             var id: String = "",
                             var model: String = "",
                             var fw_ver: String = "",
                             var support: List<String>? = null,
                             var power: String = "",
                             var bright: Int = 0,
                             var color_mode: Int = 0,
                             var ct: Int = 0,
                             var rgb: Int = 0,
                             var hue: Int = 0,
                             var sat: Int = 0,
                             var name: String = "") {

    fun getMetaData(): DeviceMetaData {
        return DeviceMetaData(model, fw_ver, support!!)
    }
}