package smarthome.raspberry.arduinodevices.script.data.digital

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.script.domain.entity.digital.DigitalCondition
import smarthome.raspberry.scripts.api.data.mapper.ConditionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.ConditionMapperResolver

@Component
class DigitalConditionMapper(
        conditionMapperResolver: ConditionMapperResolver
): ConditionDtoMapper<DigitalCondition, DigitalConditionDto> {

    init {
        conditionMapperResolver.register(DigitalCondition::class, DigitalConditionDto::class, this)
    }

    override fun mapDto(dto: DigitalConditionDto): DigitalCondition {
        return DigitalCondition(dto.id, dto.value)
    }

    override fun mapEntity(entity: DigitalCondition): DigitalConditionDto {
        return DigitalConditionDto(entity.id, entity.value)
    }
}