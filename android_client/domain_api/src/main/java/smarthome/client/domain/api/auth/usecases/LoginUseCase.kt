package smarthome.client.domain.api.auth.usecases

interface LoginUseCase {
    suspend fun execute(login: String, password: String)
}