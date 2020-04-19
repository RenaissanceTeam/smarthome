package smarthome.raspberry.scripts.api.domain

import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Condition

interface ConditionValidator {
    fun validate(condition: Condition, block: Block): Boolean
}