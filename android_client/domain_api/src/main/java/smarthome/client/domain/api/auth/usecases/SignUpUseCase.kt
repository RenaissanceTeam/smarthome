package smarthome.client.domain.api.auth.usecases

interface SignUpUseCase {
    suspend fun execute(login: String, password: String, registrationCode: Long)
}