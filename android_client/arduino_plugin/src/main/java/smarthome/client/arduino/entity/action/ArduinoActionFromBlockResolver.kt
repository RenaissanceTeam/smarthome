package smarthome.client.arduino.entity.action

import smarthome.client.arduino.entity.block.ArduinoControllerBlock
import smarthome.client.arduino.entity.onoff
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.action.Action

class ArduinoActionFromBlockResolver: ActionFromBlockResolver {
    
    override fun resolve(dependencyId: DependencyId, block: Block): Action? {
        if (block !is ArduinoControllerBlock) return null
        
        return when (block.type) {
            onoff -> OnOffAction(dependencyId = dependencyId)
            else -> null
        }
    }
    
    override fun canResolve(block: Block) = block is ArduinoControllerBlock
}