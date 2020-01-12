package smarthome.client.domain.api.conrollers.usecases

interface ReadControllerUseCase {
    suspend fun execute(id: Long): String
}