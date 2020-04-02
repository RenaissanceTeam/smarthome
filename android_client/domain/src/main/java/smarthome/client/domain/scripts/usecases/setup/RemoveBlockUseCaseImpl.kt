package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.RemoveBlockUseCase


class RemoveBlockUseCaseImpl(
    private val repo: SetupScriptRepo
) : RemoveBlockUseCase {
    override fun execute(blockId: String) {
        repo.removeBlock(blockId)
    }
}