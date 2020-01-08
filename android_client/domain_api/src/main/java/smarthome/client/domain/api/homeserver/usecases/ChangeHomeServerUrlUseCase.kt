package smarthome.client.domain.api.homeserver.usecases

interface ChangeHomeServerUrlUseCase {
    fun execute(url: String)
}