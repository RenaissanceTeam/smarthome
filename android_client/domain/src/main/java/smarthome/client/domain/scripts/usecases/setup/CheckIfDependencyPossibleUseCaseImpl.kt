package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.domain.api.scripts.usecases.setup.CheckIfDependencyPossibleUseCase
import smarthome.client.entity.script.block.BlockId

class CheckIfDependencyPossibleUseCaseImpl : CheckIfDependencyPossibleUseCase {
    override fun execute(from: BlockId, to: BlockId): Boolean {
        return true
    }
}