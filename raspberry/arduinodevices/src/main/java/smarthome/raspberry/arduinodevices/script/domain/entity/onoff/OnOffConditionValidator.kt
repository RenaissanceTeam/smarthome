package smarthome.raspberry.arduinodevices.script.domain.entity.onoff

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.script.domain.entity.ArduinoControllerBlock
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import java.util.*

@Component
class OnOffConditionValidator : ConditionValidator {
    override fun validate(condition: Condition, block: Optional<Block>): Boolean {
        if (!block.isPresent) return false
        val blockValue = block.get()

        require(condition is OnOffCondition)
        require(blockValue is ArduinoControllerBlock)


        val currentValue = blockValue.controller.state ?: return false

        return currentValue == condition.value
    }

}