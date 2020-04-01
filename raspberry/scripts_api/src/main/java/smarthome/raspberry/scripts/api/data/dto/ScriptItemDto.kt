package smarthome.raspberry.scripts.api.data.dto

data class ScriptItemDto(
        val id: Long,
        val name: String,
        val description: String,
        val enabled: Boolean
)