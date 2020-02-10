package smarthome.client.domain.scripts.usecases

import smarthome.client.domain.api.scripts.usecases.CheckIfDependencyPossibleUseCase
import smarthome.client.entity.script.block.BlockId

class CheckIfDependencyPossibleUseCaseImpl : CheckIfDependencyPossibleUseCase {
    override fun execute(scriptId: Long, from: BlockId, to: BlockId): Boolean {
        return true
    }
}