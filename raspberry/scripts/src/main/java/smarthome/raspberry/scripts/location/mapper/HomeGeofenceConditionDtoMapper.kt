package smarthome.raspberry.scripts.location.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.data.mapper.ConditionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.ConditionMapperResolver
import smarthome.raspberry.scripts.api.location.HomeGeofenceCondition
import smarthome.raspberry.scripts.location.dto.HomeGeofenceConditionDto

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