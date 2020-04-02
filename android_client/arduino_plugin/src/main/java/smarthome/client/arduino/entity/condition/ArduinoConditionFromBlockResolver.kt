package smarthome.client.arduino.entity.condition

import smarthome.client.arduino.entity.block.ArduinoControllerBlock
import smarthome.client.arduino.entity.dht
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.util.generateId

class ArduinoConditionFromBlockResolver : ConditionFromBlockResolver {
    
    override fun canResolve(item: Block): Boolean {
        return item is ArduinoControllerBlock
    }
    
    override fun resolve(item: Block): List<Condition> {
        if (item !is ArduinoControllerBlock) return emptyList()
        
        return when (item.type) {
            dht -> listOf(
                TemperatureCondition(generateId(), item.controllerId),
                HumidityCondition(generateId(), item.controllerId)
            )
            else -> emptyList()
        }
    }
}