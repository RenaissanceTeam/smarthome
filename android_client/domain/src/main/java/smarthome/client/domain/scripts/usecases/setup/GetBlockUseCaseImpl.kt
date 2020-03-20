package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.GetBlockUseCase
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.BlockId

class GetBlockUseCaseImpl(
    private val repo: SetupScriptRepo
) : GetBlockUseCase {
    override fun execute(id: BlockId): Block {
        return repo.getBlock(id) ?: throw IllegalArgumentException("No block with id $id")
    }
}