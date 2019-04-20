package smarthome.client.scripts.commonlib.scripts.conditions

import smarthome.client.scripts.commonlib.scripts.conditions.Condition

open class ExactTimeCondition : Condition() {
    var state = ""

    override fun toString() = "At time $state"

    override fun evaluate(): Boolean = true // todo
}