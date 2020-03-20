package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.block.Block

interface AddBlockToScriptGraphUseCase {
    fun execute(block: Block)
}