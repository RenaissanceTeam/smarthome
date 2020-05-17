package smarthome.raspberry.scripts.notification.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.data.mapper.BlockDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.BlockMapperResolver
import smarthome.raspberry.scripts.api.notification.NotificationBlock
import smarthome.raspberry.scripts.notification.dto.NotificationBlockDto

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