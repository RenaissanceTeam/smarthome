package smarthome.raspberry.notification.api.domain.usecase

import smarthome.raspberry.authentication.api.domain.entity.User
import smarthome.raspberry.notification.api.domain.entity.NotificationToken

interface GetNotificationTokenForUserUseCase {
    fun execute(user: User): NotificationToken
}