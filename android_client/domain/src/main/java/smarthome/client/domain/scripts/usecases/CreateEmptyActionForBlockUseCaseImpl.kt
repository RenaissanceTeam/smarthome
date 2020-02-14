package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.domain.api.scripts.usecases.CreateEmptyActionForBlockUseCase
import smarthome.client.entity.script.block.BlockId
import smarthome.client.entity.script.dependency.action.Action

class CreateEmptyActionForBlockUseCaseImpl(
    private val graphRepo: ScriptGraphRepo,
    private val actionFromBlock: ActionFromBlockResolver
) : CreateEmptyActionForBlockUseCase {
    override fun execute(scriptId: Long, blockId: BlockId): List<Action> {
        val block = graphRepo.getBlock(scriptId, blockId)
            ?: throw IllegalArgumentException("no block with id $blockId")
        
        return actionFromBlock.resolve(block)
    }
}