package smarthome.raspberry.scripts.time.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.data.mapper.ConditionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.ConditionMapperResolver
import smarthome.raspberry.scripts.api.time.TimerCondition
import smarthome.raspberry.scripts.time.dto.TimerConditionDto

@Component
class TimerConditionDtoMapper(
        conditionMapperResolver: ConditionMapperResolver
) : ConditionDtoMapper<TimerCondition, TimerConditionDto> {

    init {
        conditionMapperResolver.register(TimerCondition::class, TimerConditionDto::class, this)
    }

    override fun mapDto(dto: TimerConditionDto): TimerCondition {
        return TimerCondition(
                dto.id,
                dto.timer
        )
    }

    override fun mapEntity(entity: TimerCondition): TimerConditionDto {
        return TimerConditionDto(
                entity.id,
                entity.timer
        )
    }
}