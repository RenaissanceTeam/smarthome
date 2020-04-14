package smarthome.client.arduino.scripts.entity.condition

import smarthome.client.arduino.scripts.entity.block.ArduinoControllerBlock
import smarthome.client.arduino.scripts.entity.humidity11
import smarthome.client.arduino.scripts.entity.humidity22
import smarthome.client.arduino.scripts.entity.temperature11
import smarthome.client.arduino.scripts.entity.temperature22
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
            temperature11 -> listOf(TemperatureCondition(generateId(), item.controllerId))
            temperature22 -> listOf(TemperatureCondition(generateId(), item.controllerId))
            humidity11 -> listOf(HumidityCondition(generateId(), item.controllerId))
            humidity22 -> listOf(HumidityCondition(generateId(), item.controllerId))
            else -> emptyList()
        }
    }
}