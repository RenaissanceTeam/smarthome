package smarthome.client.domain.api.conrollers.usecases

interface WriteStateToControllerUseCase {
    suspend fun execute(controllerId: Long, state: String)
}