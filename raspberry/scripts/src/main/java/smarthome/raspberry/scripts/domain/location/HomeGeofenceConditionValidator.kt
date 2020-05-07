package smarthome.raspberry.scripts.domain.location

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import java.util.*

@Component
class HomeGeofenceConditionValidator : ConditionValidator {
    override fun validate(condition: Condition, block: Optional<Block>): Boolean {
        return false
    }
}