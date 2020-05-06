package smarthome.raspberry.scripts.domain.time

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import smarthome.raspberry.scripts.api.domain.blocks.TimeBlock
import smarthome.raspberry.scripts.api.domain.conditions.WeekdaysCondition
import java.util.*

@Component
class WeekdaysConditionValidator(
) : ConditionValidator {
    override fun validate(condition: Condition, block: Optional<Block>): Boolean {
        if (!block.isPresent) return false
        val blockValue = block.get()

        require(condition is WeekdaysCondition)
        require(blockValue is TimeBlock)

        return false
    }
}

