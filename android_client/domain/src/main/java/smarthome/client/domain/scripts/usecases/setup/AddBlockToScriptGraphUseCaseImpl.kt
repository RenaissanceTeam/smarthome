package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.AddBlockToScriptGraphUseCase
import smarthome.client.entity.script.block.Block

class AddBlockToScriptGraphUseCaseImpl(
    private val repo: SetupScriptRepo
) : AddBlockToScriptGraphUseCase {
    
    override fun execute(block: Block) {
        repo.addBlock(block)
    }
}