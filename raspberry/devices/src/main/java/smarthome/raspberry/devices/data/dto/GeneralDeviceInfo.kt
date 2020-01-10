package smarthome.raspberry.devices.data.dto

data class GeneralDeviceInfo(
        val id: Long,
        val name: String,
        val type: String,
        val controllers: List<GeneralControllerInfo>
)

