package smarthome.library.common.scripts.conditions

import smarthome.library.common.scripts.conditions.Condition

open class ExactTimeCondition : Condition() {
    var state = ""

    override fun toString() = "At time $state"

    override fun evaluate(): Boolean = true // todo
}