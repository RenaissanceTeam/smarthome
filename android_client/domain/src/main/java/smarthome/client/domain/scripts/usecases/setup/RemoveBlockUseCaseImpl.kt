package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.RemoveBlockUseCase
import smarthome.client.domain.api.scripts.usecases.setup.RemoveDependencyUseCase


class RemoveBlockUseCaseImpl(
        private val repo: SetupScriptRepo,
        private val removeDependencyUseCase: RemoveDependencyUseCase
) : RemoveBlockUseCase {
    override fun execute(blockId: String) {
        repo.getScript()
                ?.dependencies
                ?.filter { it.startBlock == blockId || it.endBlock == blockId }
                ?.forEach { removeDependencyUseCase.execute(it.id) }

        repo.removeBlock(blockId)
    }
}