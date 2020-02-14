package smarthome.client.arduino.entity.block

import smarthome.client.arduino.entity.condition.HumidityCondition
import smarthome.client.arduino.entity.condition.TemperatureCondition
import smarthome.client.arduino.entity.dht
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.condition.Condition

class ArduinoConditionFromBlockResolver : ConditionFromBlockResolver {
    
    override fun canResolve(block: Block): Boolean {
        return block is ArduinoControllerBlock
    }
    
    override fun resolve(dependencyId: DependencyId, block: Block): List<Condition> {
        if (block !is ArduinoControllerBlock) return emptyList()
        
        return when (block.type) {
            dht -> listOf(
                TemperatureCondition(block.controllerId, dependencyId),
                HumidityCondition(block.controllerId, dependencyId)
            )
            else -> emptyList()
        }
    }
}