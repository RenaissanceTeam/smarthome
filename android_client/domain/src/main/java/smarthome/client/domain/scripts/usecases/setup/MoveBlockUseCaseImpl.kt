package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.MoveBlockUseCase
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.BlockId
import smarthome.client.util.Position

class MoveBlockUseCaseImpl(
    private val repo: SetupScriptRepo
) : MoveBlockUseCase {
    
    override fun execute(blockId: String, newPosition: Position): Block {
        val block = repo.getBlocks().find { it.uuid == blockId }
            ?: throw IllegalArgumentException("can't find block with id $blockId")
        
        val movedBlock = block.copyWithPosition(newPosition)
        
        return repo.replaceBlock(movedBlock)
    }
}