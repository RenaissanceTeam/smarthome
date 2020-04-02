package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.domain.api.scripts.resolver.BlockNameResolver
import smarthome.client.domain.api.scripts.usecases.setup.GetBlockNameUseCase
import smarthome.client.domain.api.scripts.usecases.setup.GetBlockUseCase
import smarthome.client.entity.script.block.BlockId

class GetBlockNameUseCaseImpl(
    private val blockNameResolver: BlockNameResolver,
    private val getBlockUseCase: GetBlockUseCase
) : GetBlockNameUseCase {
    override fun execute(blockId: String): String {
        val block = getBlockUseCase.execute(blockId)
        return blockNameResolver.resolve(block)
    }
}