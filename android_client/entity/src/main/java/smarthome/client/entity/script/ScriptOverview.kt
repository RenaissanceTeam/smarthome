package smarthome.client.entity.script

data class ScriptOverview(
        val id: Long,
        val name: String,
        val description: String,
        val enabled: Boolean
)