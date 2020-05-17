package smarthome.raspberry.scripts.time.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.data.mapper.BlockDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.BlockMapperResolver
import smarthome.raspberry.scripts.api.time.TimeBlock
import smarthome.raspberry.scripts.time.dto.TimeBlockDto

@Component
class TimeBlockDtoMapper(
        blockMapperResolver: BlockMapperResolver
) : BlockDtoMapper<TimeBlock, TimeBlockDto> {

    init {
        blockMapperResolver.register(TimeBlock::class, TimeBlockDto::class, this)
    }

    override fun mapDto(dto: TimeBlockDto): TimeBlock {
        return TimeBlock(
                dto.id,
                dto.position
        )
    }

    override fun mapEntity(entity: TimeBlock): TimeBlockDto {
        return TimeBlockDto(
                entity.id,
                entity.position
        )
    }
}