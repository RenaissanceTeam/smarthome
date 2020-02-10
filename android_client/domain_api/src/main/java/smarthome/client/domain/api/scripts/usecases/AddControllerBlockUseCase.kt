package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.block.Block
import smarthome.client.util.Position

interface AddControllerBlockUseCase {
    fun execute(scriptId: Long, controllerId: Long, position: Position): Block
}