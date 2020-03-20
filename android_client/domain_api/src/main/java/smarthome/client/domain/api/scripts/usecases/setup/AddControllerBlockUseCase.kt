package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.block.Block
import smarthome.client.util.Position

interface AddControllerBlockUseCase {
    fun execute(controllerId: Long, position: Position): Block
}