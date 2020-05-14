package smarthome.raspberry.scripts.location

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.scripts.api.domain.ConditionState
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import java.util.*

@Component
class HomeGeofenceConditionValidator : ConditionValidator {

    override fun validate(condition: Condition, state: Optional<out ConditionState>): Boolean {
        return false
    }
}