package smarthome.raspberry.arduinodevices.script.data.dht

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.script.domain.entity.dht.TemperatureCondition
import smarthome.raspberry.scripts.api.data.mapper.ConditionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.ConditionMapperResolver

@Component
class TemperatureConditionMapper(
        conditionMapperResolver: ConditionMapperResolver
) : ConditionDtoMapper<TemperatureCondition, TemperatureConditionDto> {

    init {
        conditionMapperResolver.register(TemperatureCondition::class, TemperatureConditionDto::class, this)
    }

    override fun mapDto(dto: TemperatureConditionDto): TemperatureCondition {
        return TemperatureCondition(
                dto.id,
                dto.sign,
                dto.value
        )
    }

    override fun mapEntity(entity: TemperatureCondition): TemperatureConditionDto {
        return TemperatureConditionDto(
                entity.id,
                entity.sign,
                entity.value
        )
    }
}