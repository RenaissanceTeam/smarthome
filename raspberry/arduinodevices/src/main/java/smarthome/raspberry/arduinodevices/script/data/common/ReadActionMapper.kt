package smarthome.raspberry.arduinodevices.script.data.common


import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.script.domain.entity.common.ReadAction
import smarthome.raspberry.scripts.api.data.mapper.ActionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.ActionMapperResolver

@Component
class ReadActionMapper(
        actionMapperResolver: ActionMapperResolver
) : ActionDtoMapper<ReadAction, ReadActionDto> {
    init {
        actionMapperResolver.register(ReadAction::class, ReadActionDto::class, this)
    }

    override fun mapDto(dto: ReadActionDto): ReadAction {
        return ReadAction(dto.id)
    }

    override fun mapEntity(entity: ReadAction): ReadActionDto {
        return ReadActionDto(entity.id)
    }
}