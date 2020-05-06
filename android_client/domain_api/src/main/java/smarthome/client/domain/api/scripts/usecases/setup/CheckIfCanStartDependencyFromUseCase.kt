package smarthome.client.domain.api.scripts.usecases.setup

interface CheckIfCanStartDependencyFromUseCase {
    fun execute(blockId: String): Boolean
}