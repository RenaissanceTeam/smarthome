package smarthome.raspberry.notification_api.domain

interface SendDataNotificationUseCase {
    suspend fun execute()
}