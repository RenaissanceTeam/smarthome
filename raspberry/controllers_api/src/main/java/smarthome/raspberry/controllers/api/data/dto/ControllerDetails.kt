package smarthome.raspberry.controllers.api.data.dto

data class ControllerDetails(
        val id: Long,
        val deviceId: Long,
        val type: String,
        val name: String,
        val state: String
)