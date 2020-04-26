package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.block.Block
import smarthome.client.util.Position

interface AddBlockUseCase {
    fun execute(block: Block)
}