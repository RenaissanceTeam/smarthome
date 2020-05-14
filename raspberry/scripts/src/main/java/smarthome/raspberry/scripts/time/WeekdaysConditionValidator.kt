package smarthome.raspberry.scripts.time

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.scripts.api.domain.ConditionState
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import smarthome.raspberry.scripts.api.time.TimeBlock
import smarthome.raspberry.scripts.api.time.WeekdayStateValid
import smarthome.raspberry.scripts.api.time.conditions.WeekdaysCondition
import smarthome.raspberry.util.fold
import java.util.*

@Component
class WeekdaysConditionValidator: ConditionValidator {

    override fun validate(condition: Condition, state: Optional<out ConditionState>): Boolean {
        return state.fold(
                onNone = { false },
                onSome = {
                    require(condition is WeekdaysCondition)

                    it is WeekdayStateValid && it.condition.id == condition.id
                }
        )
    }

}

