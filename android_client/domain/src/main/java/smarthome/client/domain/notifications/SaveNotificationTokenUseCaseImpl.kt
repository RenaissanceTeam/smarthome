package smarthome.client.domain.notifications

import smarthome.client.data.api.notifications.NotificationRepository
import smarthome.client.domain.api.notifications.SaveNotificationTokenUseCase

class SaveNotificationTokenUseCaseImpl(
        private val repository: NotificationRepository
) : SaveNotificationTokenUseCase {
    override suspend fun execute(token: String) {
        repository.save(token)
    }
}