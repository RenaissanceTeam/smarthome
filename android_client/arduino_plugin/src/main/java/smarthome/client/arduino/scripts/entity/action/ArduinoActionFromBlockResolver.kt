package smarthome.client.arduino.scripts.entity.action

import smarthome.client.arduino.scripts.entity.block.ArduinoControllerBlock
import smarthome.client.arduino.scripts.entity.onoff
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.util.generateId

class ArduinoActionFromBlockResolver : ActionFromBlockResolver {
    
    override fun resolve(item: Block): List<Action> {
        if (item !is ArduinoControllerBlock) return emptyList()
        
        return when (item.type) {
            onoff -> listOf(OnOffAction(generateId()))
            else -> listOf(ReadAction(generateId()))
        }
    }
    
    override fun canResolve(item: Block) = item is ArduinoControllerBlock
}