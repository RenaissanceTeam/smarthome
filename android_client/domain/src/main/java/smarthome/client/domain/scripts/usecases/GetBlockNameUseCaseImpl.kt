package smarthome.client.domain.scripts.usecases

import smarthome.client.domain.api.scripts.resolver.BlockNameResolver
import smarthome.client.domain.api.scripts.usecases.GetBlockNameUseCase
import smarthome.client.domain.api.scripts.usecases.GetBlockUseCase
import smarthome.client.entity.script.block.BlockId

class GetBlockNameUseCaseImpl(
    private val blockNameResolver: BlockNameResolver,
    private val getBlockUseCase: GetBlockUseCase
) : GetBlockNameUseCase {
    override fun execute(scriptId: Long, blockId: BlockId): String {
        val block = getBlockUseCase.execute(scriptId, blockId)
        return blockNameResolver.resolve(block)
    }
}