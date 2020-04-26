package smarthome.raspberry.arduinodevices.script.data.dht

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.script.domain.entity.dht.HumidityCondition
import smarthome.raspberry.scripts.api.data.mapper.ConditionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.ConditionMapperResolver

@Component
class HumidityConditionMapper(
        conditionMapperResolver: ConditionMapperResolver
) : ConditionDtoMapper<HumidityCondition, HumidityConditionDto> {

    init {
        conditionMapperResolver.register(HumidityCondition::class, HumidityConditionDto::class, this)
    }

    override fun mapDto(dto: HumidityConditionDto): HumidityCondition {
        return HumidityCondition(
                dto.id,
                dto.sign,
                dto.value
        )
    }

    override fun mapEntity(entity: HumidityCondition): HumidityConditionDto {
        return HumidityConditionDto(
                entity.id,
                entity.sign,
                entity.value
        )
    }
}

