package smarthome.raspberry.scripts.location.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.data.mapper.BlockDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.BlockMapperResolver
import smarthome.raspberry.scripts.api.location.LocationBlock
import smarthome.raspberry.scripts.location.dto.LocationBlockDto

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