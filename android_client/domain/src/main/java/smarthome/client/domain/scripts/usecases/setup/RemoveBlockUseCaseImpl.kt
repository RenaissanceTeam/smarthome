package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.RemoveBlockUseCase
import smarthome.client.entity.script.block.BlockId

class RemoveBlockUseCaseImpl(
    private val repo: SetupScriptRepo
) : RemoveBlockUseCase {
    override fun execute(blockId: BlockId) {
        repo.removeBlock(blockId)
    }
}