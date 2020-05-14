package smarthome.raspberry.arduinodevices.script.domain.entity.dht

import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.controllers.ControllerConditionState
import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.scripts.api.domain.ConditionState
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import smarthome.raspberry.util.fold
import java.util.*

@Component
class TemperatureConditionValidator : ConditionValidator {

    override fun validate(condition: Condition, state: Optional<out ConditionState>): Boolean {
        return state.fold(
                onNone = { false },
                onSome = {
                    require(condition is TemperatureCondition)
                    require(it is ControllerConditionState)


                    val conditionValue = condition.value.toDoubleOrNull() ?: return@fold false
                    val currentValue = it.controller.state?.toDoubleOrNull() ?: return@fold false

                    when (condition.sign) {
                        ">" -> currentValue > conditionValue
                        "=" -> currentValue == conditionValue
                        "<" -> currentValue < conditionValue
                        else -> false
                    }

                }
        )
    }
}

