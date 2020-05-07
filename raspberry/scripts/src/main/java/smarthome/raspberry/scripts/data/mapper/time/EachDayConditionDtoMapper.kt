package smarthome.raspberry.scripts.data.mapper.time

import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.data.mapper.ConditionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.ConditionMapperResolver
import smarthome.raspberry.scripts.api.domain.time.EachDayCondition
import smarthome.raspberry.scripts.data.dto.time.EachDayConditionDto

@Component
class EachDayConditionDtoMapper(
        conditionMapperResolver: ConditionMapperResolver
) : ConditionDtoMapper<EachDayCondition, EachDayConditionDto> {

    init {
        conditionMapperResolver.register(EachDayCondition::class, EachDayConditionDto::class, this)
    }

    override fun mapDto(dto: EachDayConditionDto): EachDayCondition {
        return EachDayCondition(
                dto.id,
                dto.time
        )
    }

    override fun mapEntity(entity: EachDayCondition): EachDayConditionDto {
        return EachDayConditionDto(
                entity.id,
                entity.time
        )
    }
}