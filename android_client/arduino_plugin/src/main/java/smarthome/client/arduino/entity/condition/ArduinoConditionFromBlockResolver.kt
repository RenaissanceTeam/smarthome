package smarthome.client.arduino.entity.condition

import smarthome.client.arduino.entity.block.ArduinoControllerBlock
import smarthome.client.arduino.entity.dht
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.entity.script.dependency.condition.SimpleDependencyUnitId

class ArduinoConditionFromBlockResolver : ConditionFromBlockResolver {
    
    override fun canResolve(block: Block): Boolean {
        return block is ArduinoControllerBlock
    }
    
    override fun resolve(block: Block): List<Condition> {
        if (block !is ArduinoControllerBlock) return emptyList()
        
        val data = when (block.type) {
            dht -> listOf(
                TemperatureConditionData(block.controllerId),
                HumidityConditionData(block.controllerId)
            )
            else -> emptyList()
        }
        
        return data.map { Condition(SimpleDependencyUnitId(), it) }
    }
}