package smarthome.raspberry.scripts.api.domain

import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Condition
import java.util.*

interface ConditionValidator {
    fun validate(
            condition: Condition,
            block: Optional<Block>
    ): Boolean
}