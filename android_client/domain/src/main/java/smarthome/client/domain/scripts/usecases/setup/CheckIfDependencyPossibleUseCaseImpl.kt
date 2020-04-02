package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.domain.api.scripts.usecases.setup.CheckIfDependencyPossibleUseCase
import smarthome.client.entity.script.block.BlockId

class CheckIfDependencyPossibleUseCaseImpl : CheckIfDependencyPossibleUseCase {
    override fun execute(from: String, to: String): Boolean {
        return true
    }
}