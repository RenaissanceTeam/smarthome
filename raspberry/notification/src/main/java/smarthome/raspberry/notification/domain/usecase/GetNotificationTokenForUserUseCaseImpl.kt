package smarthome.raspberry.notification.domain.usecase

import org.springframework.stereotype.Component
import smarthome.raspberry.authentication.api.domain.entity.User
import smarthome.raspberry.notification.NotificationTokenRepository
import smarthome.raspberry.notification.api.domain.entity.NotificationToken
import smarthome.raspberry.notification.api.domain.usecase.GetNotificationTokenForUserUseCase
import smarthome.raspberry.util.exceptions.notFound

@Component
class GetNotificationTokenForUserUseCaseImpl(
        private val repository: NotificationTokenRepository
) : GetNotificationTokenForUserUseCase {
    override fun execute(user: User): NotificationToken {
        return repository.findByUserUsername(user.username) ?: throw notFound
    }
}