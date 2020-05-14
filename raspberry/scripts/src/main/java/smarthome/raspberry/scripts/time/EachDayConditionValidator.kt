package smarthome.raspberry.scripts.time

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.scripts.api.domain.ConditionState
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import smarthome.raspberry.scripts.api.time.EachDayStateValid
import smarthome.raspberry.scripts.api.time.conditions.EachDayCondition
import smarthome.raspberry.util.fold
import java.util.*

@Component
class EachDayConditionValidator: ConditionValidator {

    override fun validate(condition: Condition, state: Optional<out ConditionState>): Boolean {
        return state.fold(
                onNone = { false },
                onSome = {
                    require(condition is EachDayCondition)

                    it is EachDayStateValid && it.condition.id == condition.id
                }
        )
    }
}
