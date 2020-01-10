package smarthome.client.domain.api.auth.usecases

interface SaveNewTokenUseCase {
    fun execute(token: String)
}
