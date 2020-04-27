package smarthome.raspberry.arduinodevices.script.data.analog

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.script.domain.entity.analog.AnalogCondition
import smarthome.raspberry.scripts.api.data.mapper.ConditionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.ConditionMapperResolver

@Component
class AnalogConditionMapper(
        conditionMapperResolver: ConditionMapperResolver
): ConditionDtoMapper<AnalogCondition, AnalogConditionDto> {
    init {
        conditionMapperResolver.register(AnalogCondition::class, AnalogConditionDto::class, this)
    }

    override fun mapDto(dto: AnalogConditionDto): AnalogCondition {
        return AnalogCondition(
                dto.id,
                dto.value
        )
    }

    override fun mapEntity(entity: AnalogCondition): AnalogConditionDto {
        return AnalogConditionDto(
                entity.id,
                entity.value
        )
    }
}