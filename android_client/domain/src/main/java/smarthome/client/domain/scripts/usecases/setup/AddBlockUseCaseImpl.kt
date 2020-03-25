package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.AddBlockUseCase
import smarthome.client.entity.script.block.Block

class AddBlockUseCaseImpl(
    private val repo: SetupScriptRepo
) : AddBlockUseCase {
    override fun execute(block: Block) {
        repo.addBlock(block)
    }
}