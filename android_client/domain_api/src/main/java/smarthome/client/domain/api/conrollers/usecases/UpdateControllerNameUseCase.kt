package smarthome.client.domain.api.conrollers.usecases

interface UpdateControllerNameUseCase {
    suspend fun execute(id: Long, name: String)
}