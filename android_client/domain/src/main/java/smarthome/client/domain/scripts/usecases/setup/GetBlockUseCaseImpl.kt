package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.GetBlockUseCase
import smarthome.client.entity.script.block.Block


class GetBlockUseCaseImpl(
    private val repo: SetupScriptRepo
) : GetBlockUseCase {
    override fun execute(uuid: String): Block {
        return repo.getBlock(uuid) ?: throw IllegalArgumentException("No block with id $uuid")
    }
}