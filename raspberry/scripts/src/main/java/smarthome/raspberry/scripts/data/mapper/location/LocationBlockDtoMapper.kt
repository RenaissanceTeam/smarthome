package smarthome.raspberry.scripts.data.mapper.location

import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.data.mapper.BlockDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.BlockMapperResolver
import smarthome.raspberry.scripts.api.domain.location.LocationBlock
import smarthome.raspberry.scripts.data.dto.location.LocationBlockDto

@Component
class LocationBlockDtoMapper(
        blockMapperResolver: BlockMapperResolver
) : BlockDtoMapper<LocationBlock, LocationBlockDto> {
    init {
        blockMapperResolver.register(LocationBlock::class, LocationBlockDto::class, this)
    }

    override fun mapDto(dto: LocationBlockDto): LocationBlock {
        return LocationBlock(
                dto.id,
                dto.position
        )
    }

    override fun mapEntity(entity: LocationBlock): LocationBlockDto {
        return LocationBlockDto(
                entity.id,
                entity.position
        )
    }
}