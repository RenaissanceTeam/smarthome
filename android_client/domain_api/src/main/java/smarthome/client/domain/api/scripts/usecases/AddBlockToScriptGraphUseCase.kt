package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.block.Block

interface AddBlockToScriptGraphUseCase {
    fun execute(scriptId: Long, block: Block)
}