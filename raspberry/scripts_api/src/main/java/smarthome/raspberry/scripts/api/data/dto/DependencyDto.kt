package smarthome.raspberry.scripts.api.data.dto

open class DependencyDto(
        open val id: String,
        open val startBlock: String,
        open val endBlock: String,
        val conditions: List<ConditionDto>,
        val actions: List<ActionDto>
)