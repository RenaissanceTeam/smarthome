package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.usecases.MoveBlockUseCase
import smarthome.client.entity.script.Block
import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Position

class MoveBlockUseCaseImpl(
    private val repo: ScriptGraphRepo
) : MoveBlockUseCase {
    
    override fun execute(scriptId: Long, blockId: BlockId, newPosition: Position): Block {
        val block = repo.getBlocks(scriptId).find { it.id == blockId }
            ?: throw IllegalArgumentException("can't find block with id $blockId")
        
        val movedBlock = block.copyWithPosition(newPosition)
        
        return repo.replaceBlock(movedBlock)
    }
}