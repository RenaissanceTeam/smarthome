package smarthome.client.domain.api.scripts.usecases.setup

interface RemoveBlockUseCase {
    fun execute(blockId: String)
}