package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.conrollers.usecases.GetControllerUseCase
import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.domain.api.scripts.usecases.setup.AddControllerBlockUseCase
import smarthome.client.entity.script.block.Block
import smarthome.client.util.Position

class AddControllerBlockUseCaseImpl(
    private val getControllerUseCase: GetControllerUseCase,
    private val blockResolver: ControllerBlockResolver,
    private val repo: SetupScriptRepo
) : AddControllerBlockUseCase {
    override fun execute(controllerId: Long, position: Position): Block {
        val controller = getControllerUseCase.execute(controllerId)
        val block = blockResolver.resolve(controller, position)
            ?: throw IllegalArgumentException("can't create block for controller $controllerId")
        repo.addBlock(block)
        return block
    }
}