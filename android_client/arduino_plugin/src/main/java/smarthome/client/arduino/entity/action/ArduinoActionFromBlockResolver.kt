package smarthome.client.arduino.entity.action

import smarthome.client.arduino.entity.block.ArduinoControllerBlock
import smarthome.client.arduino.entity.onoff
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.SimpleDependencyUnitId

class ArduinoActionFromBlockResolver: ActionFromBlockResolver {
    
    override fun resolve(block: Block): List<Action> {
        if (block !is ArduinoControllerBlock) return emptyList()
        
        val data = when (block.type) {
            onoff -> listOf(OnOffActionData())
            else -> emptyList()
        }
        
        return data.map { Action(SimpleDependencyUnitId(), it) }
    }
    
    override fun canResolve(block: Block) = block is ArduinoControllerBlock
}