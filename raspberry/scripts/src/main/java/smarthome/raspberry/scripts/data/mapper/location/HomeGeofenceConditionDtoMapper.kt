package smarthome.raspberry.scripts.data.mapper.location

import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.data.mapper.ConditionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.ConditionMapperResolver
import smarthome.raspberry.scripts.api.domain.location.HomeGeofenceCondition
import smarthome.raspberry.scripts.data.dto.location.HomeGeofenceConditionDto

@Component
class HomeGeofenceConditionDtoMapper(
        conditionMapperResolver: ConditionMapperResolver
) : ConditionDtoMapper<HomeGeofenceCondition, HomeGeofenceConditionDto> {

    init {
        conditionMapperResolver.register(HomeGeofenceCondition::class, HomeGeofenceConditionDto::class, this)
    }

    override fun mapDto(dto: HomeGeofenceConditionDto): HomeGeofenceCondition {
        return HomeGeofenceCondition(
                dto.id,
                dto.inside
        )
    }

    override fun mapEntity(entity: HomeGeofenceCondition): HomeGeofenceConditionDto {
        return HomeGeofenceConditionDto(
                entity.id,
                entity.inside
        )
    }
}