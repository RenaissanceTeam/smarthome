package smarthome.raspberry.scripts.api.data.dto

import smarthome.raspberry.entity.ID_NOT_DEFINED

open class ScriptDto(
        val id: Long = ID_NOT_DEFINED,
        val name: String,
        val description: String,
        val enabled: Boolean = true,
        val blocks: List<BlockDto>,
        val dependencies: List<DependencyDto>
)