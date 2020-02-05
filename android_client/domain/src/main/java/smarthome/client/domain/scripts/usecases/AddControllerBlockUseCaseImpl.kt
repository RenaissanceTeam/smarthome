package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.usecases.AddControllerBlockUseCase
import smarthome.client.entity.script.Block
import smarthome.client.entity.script.Position
import smarthome.client.entity.script.controller.ControllerBlock

class AddControllerBlockUseCaseImpl(
    private val repo: ScriptGraphRepo
) : AddControllerBlockUseCase {
    override fun execute(scriptId: Long, controllerId: Long, position: Position): Block {
        return repo.addBlock(scriptId, ControllerBlock(controllerId, position))
    }
}