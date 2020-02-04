package smarthome.client.data.scripts.mapper

import smarthome.client.data.scripts.dto.ScriptDto
import smarthome.client.entity.script.Script

class ScriptDtoToScriptMapper {
    fun map(script: ScriptDto) = Script(
        id = script.id,
        name = script.name
    )
}