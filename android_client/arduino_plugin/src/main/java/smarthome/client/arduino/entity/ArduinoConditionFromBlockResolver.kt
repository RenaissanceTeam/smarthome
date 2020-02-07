package smarthome.client.arduino.entity

import smarthome.client.arduino.entity.block.ControllerBlock
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.condition.Condition

class ArduinoConditionFromBlockResolver : ConditionFromBlockResolver {
    
    override fun canResolve(block: Block): Boolean {
        return block is ControllerBlock
    }
    
    override fun resolve(dependencyId: DependencyId, block: Block): List<Condition> {
        if (block !is ControllerBlock) return emptyList()
        
        return when (block.type) {
            "dht" -> listOf(TemperatureCondition(block.controllerId, dependencyId), HumidityCondition(block.controllerId, dependencyId))
            else -> emptyList()
        }
    }
}