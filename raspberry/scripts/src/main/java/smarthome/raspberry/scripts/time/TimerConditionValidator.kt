package smarthome.raspberry.scripts.time

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import smarthome.raspberry.scripts.api.time.TimeBlock
import smarthome.raspberry.scripts.api.time.TimerCondition
import java.util.*

@Component
class TimerConditionValidator(
) : ConditionValidator {
    override fun validate(condition: Condition, block: Optional<Block>): Boolean {
        if (!block.isPresent) return false
        val blockValue = block.get()

        require(condition is TimerCondition)
        require(blockValue is TimeBlock)

        return false
    }
}