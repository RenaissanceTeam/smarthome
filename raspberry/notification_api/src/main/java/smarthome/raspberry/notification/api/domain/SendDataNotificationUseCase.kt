package smarthome.raspberry.notification.api.domain

interface SendDataNotificationUseCase {
    suspend fun execute()
}