package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.usecases.GetBlockUseCase
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.BlockId

class GetBlockUseCaseImpl(
    private val repo: ScriptGraphRepo
) : GetBlockUseCase {
    override fun execute(scriptId: Long, id: BlockId): Block {
        return repo.getBlock(scriptId, id) ?: throw IllegalArgumentException("No block with id $id")
    }
}