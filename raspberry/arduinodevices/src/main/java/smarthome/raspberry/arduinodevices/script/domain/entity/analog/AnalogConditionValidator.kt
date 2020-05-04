package smarthome.raspberry.arduinodevices.script.domain.entity.analog

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.script.domain.entity.ArduinoControllerBlock
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import java.util.*


@Component
class AnalogConditionValidator : ConditionValidator {

    override fun validate(condition: Condition, block: Optional<Block>): Boolean {
        if (!block.isPresent) return false
        val blockValue = block.get()

        require(condition is AnalogCondition)
        require(blockValue is ArduinoControllerBlock)


        val conditionValue = condition.value.toDoubleOrNull() ?: return false
        val currentValue = blockValue.controller.state?.toDoubleOrNull() ?: return false

        return currentValue == conditionValue
    }
}