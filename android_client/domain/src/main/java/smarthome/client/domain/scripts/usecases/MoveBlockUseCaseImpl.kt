package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.usecases.MoveBlockUseCase
import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Position

class MoveBlockUseCaseImpl(
    private val repo: ScriptGraphRepo
) : MoveBlockUseCase {
    
    override fun execute(scriptId: Long, blockId: BlockId, newPosition: Position) {
        val block = repo.getBlocks(scriptId).find { it.id == blockId } ?: return
        
        val movedBlock = block.copyWithPosition(newPosition)
        
        repo.replaceBlock(movedBlock)
    }
}