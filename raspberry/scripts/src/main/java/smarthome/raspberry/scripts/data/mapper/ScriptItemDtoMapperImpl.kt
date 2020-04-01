package smarthome.raspberry.scripts.data.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.data.dto.ScriptItemDto
import smarthome.raspberry.scripts.api.data.mapper.ScriptItemDtoMapper

@Component
class ScriptItemDtoMapperImpl : ScriptItemDtoMapper {
    override fun map(entity: Script) = ScriptItemDto(
            id = entity.id,
            enabled = entity.enabled,
            description = entity.description,
            name = entity.name
    )
}