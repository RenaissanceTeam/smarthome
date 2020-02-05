package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.usecases.RemoveBlockUseCase
import smarthome.client.entity.script.BlockId

class RemoveBlockUseCaseImpl(
    private val repo: ScriptGraphRepo
) : RemoveBlockUseCase {
    override fun execute(scriptId: Long, blockId: BlockId) {
        repo.removeBlock(scriptId, blockId)
    }
}