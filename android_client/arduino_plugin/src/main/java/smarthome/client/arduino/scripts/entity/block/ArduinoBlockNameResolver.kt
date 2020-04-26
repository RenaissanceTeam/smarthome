package smarthome.client.arduino.scripts.entity.block

import smarthome.client.domain.api.conrollers.usecases.GetControllerUseCase
import smarthome.client.domain.api.scripts.resolver.BlockNameResolver
import smarthome.client.entity.script.block.Block

class ArduinoBlockNameResolver(
    private val getControllerUseCase: GetControllerUseCase
) : BlockNameResolver {
    override fun canResolve(item: Block): Boolean {
        return item is ArduinoControllerBlock
    }
    
    override fun resolve(item: Block): String {
        require(item is ArduinoControllerBlock)
    
        return getControllerUseCase.execute(item.controllerId).name
    }
}