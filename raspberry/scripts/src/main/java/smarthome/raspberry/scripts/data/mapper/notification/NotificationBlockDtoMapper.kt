package smarthome.raspberry.scripts.data.mapper.notification

import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.data.mapper.BlockDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.BlockMapperResolver
import smarthome.raspberry.scripts.api.domain.blocks.NotificationBlock
import smarthome.raspberry.scripts.data.dto.notification.NotificationBlockDto

@Component
class NotificationBlockDtoMapper(
        blockMapperResolver: BlockMapperResolver
) : BlockDtoMapper<NotificationBlock, NotificationBlockDto> {

    init {
        blockMapperResolver.register(NotificationBlock::class, NotificationBlockDto::class, this)
    }

    override fun mapDto(dto: NotificationBlockDto): NotificationBlock {
        return NotificationBlock(
                id = dto.id,
                position = dto.position
        )
    }

    override fun mapEntity(entity: NotificationBlock): NotificationBlockDto {
        return NotificationBlockDto(
                id = entity.id,
                position = entity.position
        )
    }
}