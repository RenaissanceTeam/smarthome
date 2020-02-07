package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.usecases.AddBlockToScriptGraphUseCase
import smarthome.client.entity.script.block.Block

class AddBlockToScriptGraphUseCaseImpl(
    private val repo: ScriptGraphRepo
) : AddBlockToScriptGraphUseCase {
    
    override fun execute(scriptId: Long, block: Block) {
        repo.addBlock(scriptId, block)
    }
}