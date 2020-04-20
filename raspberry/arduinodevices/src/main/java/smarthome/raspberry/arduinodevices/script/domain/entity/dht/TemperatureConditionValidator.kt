package smarthome.raspberry.arduinodevices.script.domain.entity.dht

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.script.domain.entity.ArduinoControllerBlock
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.scripts.api.domain.ConditionValidator

@Component
class TemperatureConditionValidator : ConditionValidator {

    override fun validate(condition: Condition, block: Block): Boolean {
        require(condition is TemperatureCondition)
        require(block is ArduinoControllerBlock)


        val conditionValue = condition.value.toDoubleOrNull() ?: return false
        val currentValue = block.controller.state?.toDoubleOrNull() ?: return false

        return when (condition.sign) {
            ">" -> currentValue > conditionValue
            "=" -> currentValue == conditionValue
            "<" -> currentValue < conditionValue
            else -> return false
        }
    }
}