package smarthome.raspberry.scripts.api.data.mapper

import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.data.dto.ScriptItemDto

interface ScriptItemDtoMapper {
    fun map(entity: Script): ScriptItemDto
}