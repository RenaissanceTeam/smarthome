package smarthome.raspberry.scripts.time.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.data.mapper.ConditionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.ConditionMapperResolver
import smarthome.raspberry.scripts.api.time.WeekdaysCondition
import smarthome.raspberry.scripts.time.dto.WeekdaysConditionDto

@Component
class WeekdaysConditionDtoMapper(
        conditionMapperResolver: ConditionMapperResolver
) : ConditionDtoMapper<WeekdaysCondition, WeekdaysConditionDto> {

    init {
        conditionMapperResolver.register(WeekdaysCondition::class, WeekdaysConditionDto::class, this)
    }

    override fun mapDto(dto: WeekdaysConditionDto): WeekdaysCondition {
        return WeekdaysCondition(
                dto.id,
                dto.days,
                dto.time
        )
    }

    override fun mapEntity(entity: WeekdaysCondition): WeekdaysConditionDto {
        return WeekdaysConditionDto(
                entity.id,
                entity.days,
                entity.time
        )
    }
}