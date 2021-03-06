package smarthome.raspberry.scripts.notification.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.authentication.api.domain.usecase.GetUserByUsernameUseCase
import smarthome.raspberry.scripts.api.data.mapper.ActionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.ActionMapperResolver
import smarthome.raspberry.scripts.api.notification.SendNotificationAction
import smarthome.raspberry.scripts.notification.dto.SendNotificationActionDto

@Component
class SendNotificationActionDtoMapper(
        actionMapperResolver: ActionMapperResolver,
        private val getUserByUsernameUseCase: GetUserByUsernameUseCase
) : ActionDtoMapper<SendNotificationAction, SendNotificationActionDto> {

    init {
        actionMapperResolver.register(SendNotificationAction::class, SendNotificationActionDto::class, this)
    }

    override fun mapDto(dto: SendNotificationActionDto): SendNotificationAction {
        return SendNotificationAction(
                id = dto.id,
                user = getUserByUsernameUseCase.execute(dto.user),
                message = dto.message
        )
    }

    override fun mapEntity(entity: SendNotificationAction): SendNotificationActionDto {
        return SendNotificationActionDto(
                id = entity.id,
                message = entity.message,
                user = entity.user.username
        )
    }
}