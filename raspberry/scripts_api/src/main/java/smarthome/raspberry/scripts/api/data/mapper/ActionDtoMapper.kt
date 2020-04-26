package smarthome.raspberry.scripts.api.data.mapper

import smarthome.raspberry.entity.script.Action
import smarthome.raspberry.json.mapper.DtoMapper
import smarthome.raspberry.scripts.api.data.dto.ActionDto

interface ActionDtoMapper<E : Action, D : ActionDto> : DtoMapper<E, D>