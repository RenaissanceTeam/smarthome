package smarthome.client.domain.api.scripts.usecases.setup

interface GetBlockNameUseCase {
    fun execute(blockId: String): String
}