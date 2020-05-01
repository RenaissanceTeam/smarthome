package smarthome.client.data.api.notifications

interface NotificationRepository {
    suspend fun save(token: String)
}