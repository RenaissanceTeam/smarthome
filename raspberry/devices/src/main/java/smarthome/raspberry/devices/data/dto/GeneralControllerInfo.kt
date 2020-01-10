package smarthome.raspberry.devices.data.dto

data class GeneralControllerInfo(
        val id: Long,
        val deviceId: Long,
        val type: String,
        val name: String,
        val state: String
)