package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.Block
import smarthome.client.entity.script.Position

interface AddControllerBlockUseCase {
    fun execute(scriptId: Long, controllerId: Long, position: Position): Block
}