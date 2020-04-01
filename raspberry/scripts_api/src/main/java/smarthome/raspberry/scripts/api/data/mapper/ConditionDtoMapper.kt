package smarthome.raspberry.scripts.api.data.mapper

import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.json.mapper.DtoMapper
import smarthome.raspberry.scripts.api.data.dto.ConditionDto

interface ConditionDtoMapper<E : Condition, D : ConditionDto> : DtoMapper<E, D>