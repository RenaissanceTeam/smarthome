package smarthome.client.domain.conrollers.usecases

import smarthome.client.domain.api.conrollers.usecases.WriteStateToControllerUseCase

class WriteStateToControllerUseCaseImpl : WriteStateToControllerUseCase {
    override suspend fun execute(controllerId: Long, state: String) {
    
    }
}