package smarthome.client.domain.api.devices.dto

data class GeneralDeviceInfo(
    val id: Long,
    val name: String,
    val type: String,
    val controllers: List<Long>
)

