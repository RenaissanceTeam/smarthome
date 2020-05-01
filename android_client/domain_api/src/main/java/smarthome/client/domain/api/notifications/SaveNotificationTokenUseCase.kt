package smarthome.client.domain.api.notifications

interface SaveNotificationTokenUseCase {
    suspend fun execute(token: String)
}