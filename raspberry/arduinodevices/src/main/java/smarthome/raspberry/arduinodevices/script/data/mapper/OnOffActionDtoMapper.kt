package smarthome.raspberry.arduinodevices.script.data.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.script.data.dto.OnOffActionDto
import smarthome.raspberry.arduinodevices.script.domain.entity.onoff.OnOffAction
import smarthome.raspberry.scripts.api.data.mapper.ActionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.ActionMapperResolver

@Component
class OnOffActionDtoMapper(
        actionMapperResolver: ActionMapperResolver
) : ActionDtoMapper<OnOffAction, OnOffActionDto> {

    init {
        actionMapperResolver.register(OnOffAction::class, OnOffActionDto::class, this)
    }

    override fun mapDto(dto: OnOffActionDto): OnOffAction {
        return OnOffAction(
                dto.id,
                dto.value
        )
    }

    override fun mapEntity(entity: OnOffAction): OnOffActionDto {
        return OnOffActionDto(
                entity.id,
                entity.value
        )
    }
}