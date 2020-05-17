package smarthome.raspberry.scripts.time

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.scripts.api.domain.ConditionState
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import smarthome.raspberry.scripts.api.time.TimerStateValid
import smarthome.raspberry.scripts.api.time.WeekdayStateValid
import smarthome.raspberry.scripts.api.time.conditions.TimerCondition
import smarthome.raspberry.scripts.api.time.conditions.WeekdaysCondition
import smarthome.raspberry.util.fold
import java.util.*

@Component
class TimerConditionValidator : ConditionValidator {

    override fun validate(condition: Condition, state: Optional<out ConditionState>): Boolean {
        return state.fold(
                onNone = { false },
                onSome = {
                    require(condition is TimerCondition)

                    it is TimerStateValid && it.condition.id == condition.id
                }
        )
    }
}