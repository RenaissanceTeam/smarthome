package smarthome.raspberry.notification.domain.usecase

import org.springframework.stereotype.Component
import smarthome.raspberry.authentication.api.domain.entity.User
import smarthome.raspberry.authentication.api.domain.usecase.GetCallingUserUseCase
import smarthome.raspberry.notification.NotificationTokenRepository
import smarthome.raspberry.notification.api.domain.entity.NotificationToken
import smarthome.raspberry.notification.api.domain.usecase.SaveNotificationTokenUseCase

@Component
class SaveNotificationTokenUseCaseImpl(
        private val getCallingUserUseCase: GetCallingUserUseCase,
        private val repository: NotificationTokenRepository
) : SaveNotificationTokenUseCase {

    override fun execute(token: String) {
        val user = getCallingUserUseCase.execute()

        repository.save(getOrCreateToken(user).copy(token = token))
    }

    private fun getOrCreateToken(user: User): NotificationToken {
        return (repository.findByUserUsername(user.username) ?: NotificationToken(user = user))
    }
}