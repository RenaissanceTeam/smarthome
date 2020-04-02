package smarthome.client.arduino.entity.action

import smarthome.client.arduino.entity.block.ArduinoControllerBlock
import smarthome.client.arduino.entity.onoff
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.util.generateId

class ArduinoActionFromBlockResolver : ActionFromBlockResolver {
    
    override fun resolve(item: Block): List<Action> {
        if (item !is ArduinoControllerBlock) return emptyList()
        
        val data = when (item.type) {
            onoff -> listOf(OnOffActionData())
            else -> emptyList()
        }
        
        return data.map { Action(generateId(), it) }
    }
    
    override fun canResolve(item: Block) = item is ArduinoControllerBlock
}