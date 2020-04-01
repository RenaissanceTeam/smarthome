package smarthome.raspberry.scripts.api.data.mapper

import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.json.mapper.DtoMapper
import smarthome.raspberry.scripts.api.data.dto.ScriptDto

interface ScriptDtoMapper<E : Script, D : ScriptDto> : DtoMapper<E, D>