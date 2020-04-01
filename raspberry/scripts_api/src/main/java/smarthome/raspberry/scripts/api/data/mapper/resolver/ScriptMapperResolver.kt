package smarthome.raspberry.scripts.api.data.mapper.resolver

import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.json.mapper.MapperResolver
import smarthome.raspberry.scripts.api.data.dto.ScriptDto
import smarthome.raspberry.scripts.api.data.mapper.ScriptDtoMapper

abstract class ScriptMapperResolver : MapperResolver<Script, ScriptDto, ScriptDtoMapper<Script, ScriptDto>>()

