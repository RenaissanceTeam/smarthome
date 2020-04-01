package smarthome.raspberry.scripts.api.data.mapper.resolver

import smarthome.raspberry.entity.script.Action
import smarthome.raspberry.json.mapper.MapperResolver
import smarthome.raspberry.scripts.api.data.dto.ActionDto
import smarthome.raspberry.scripts.api.data.mapper.ActionDtoMapper

abstract class ActionMapperResolver : MapperResolver<Action, ActionDto, ActionDtoMapper<out Action, out ActionDto>>()