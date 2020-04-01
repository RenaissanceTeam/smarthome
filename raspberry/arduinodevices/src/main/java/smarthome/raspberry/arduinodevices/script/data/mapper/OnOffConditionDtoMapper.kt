package smarthome.raspberry.arduinodevices.script.data.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.script.data.dto.OnOffConditionDto
import smarthome.raspberry.arduinodevices.script.domain.entity.onoff.OnOffCondition
import smarthome.raspberry.scripts.api.data.mapper.ConditionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.ConditionMapperResolver

@Component
class OnOffConditionDtoMapper(
        conditionMapperResolver: ConditionMapperResolver
) : ConditionDtoMapper<OnOffCondition, OnOffConditionDto> {

    init {
        conditionMapperResolver.register(OnOffCondition::class, OnOffConditionDto::class, this)
    }

    override fun mapDto(dto: OnOffConditionDto): OnOffCondition {
        return OnOffCondition(
                dto.id,
                dto.value
        )
    }

    override fun mapEntity(entity: OnOffCondition): OnOffConditionDto {
        return OnOffConditionDto(
                entity.id,
                entity.value
        )
    }
}