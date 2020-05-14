package smarthome.raspberry.arduinodevices.script.domain.entity.onoff

import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.controllers.ControllerConditionState
import smarthome.raspberry.arduinodevices.script.domain.entity.dht.HumidityCondition
import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.scripts.api.domain.ConditionState
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import smarthome.raspberry.util.fold
import java.util.*

@Component
class OnOffConditionValidator : ConditionValidator {
    override fun validate(condition: Condition, state: Optional<out ConditionState>): Boolean {

        return state.fold(
                onNone = { false },
                onSome = {
                    require(condition is HumidityCondition)
                    require(it is ControllerConditionState)


                    val currentValue = it.controller.state ?: return@fold false

                    currentValue == condition.value
                }
        )
    }
}
