package smarthome.client.domain.api.homeserver.usecases

interface ChangeHomeServerUrlUseCase {
    suspend fun execute(url: String): Boolean
}