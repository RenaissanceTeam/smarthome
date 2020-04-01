package smarthome.raspberry.scripts.api.data.mapper.resolver

import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.json.mapper.MapperResolver
import smarthome.raspberry.scripts.api.data.dto.ConditionDto
import smarthome.raspberry.scripts.api.data.mapper.ConditionDtoMapper

abstract class ConditionMapperResolver : MapperResolver<Condition, ConditionDto, ConditionDtoMapper<out Condition, out ConditionDto>>()