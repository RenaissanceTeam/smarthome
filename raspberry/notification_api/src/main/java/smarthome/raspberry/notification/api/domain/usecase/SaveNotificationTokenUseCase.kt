package smarthome.raspberry.notification.api.domain.usecase

interface SaveNotificationTokenUseCase {
    fun execute(token: String)
}