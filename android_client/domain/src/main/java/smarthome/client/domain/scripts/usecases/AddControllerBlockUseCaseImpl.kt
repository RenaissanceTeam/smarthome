package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.conrollers.usecases.GetControllerUseCase
import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.domain.api.scripts.usecases.AddControllerBlockUseCase
import smarthome.client.entity.script.block.Block
import smarthome.client.util.Position

class AddControllerBlockUseCaseImpl(
    private val getControllerUseCase: GetControllerUseCase,
    private val blockResolver: ControllerBlockResolver,
    private val repo: ScriptGraphRepo
) : AddControllerBlockUseCase {
    override fun execute(scriptId: Long, controllerId: Long, position: Position): Block {
        val controller = getControllerUseCase.execute(controllerId)
        val block = blockResolver.resolve(controller, position)
            ?: throw IllegalArgumentException("can't create block for controller $controllerId")
        repo.addBlock(scriptId, block)
        return block
    }
}