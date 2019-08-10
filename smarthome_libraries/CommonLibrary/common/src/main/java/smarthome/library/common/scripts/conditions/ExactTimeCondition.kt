package smarthome.library.common.scripts.conditions

open class ExactTimeCondition : Condition() {
    var state = ""

    override fun toString() = "At time $state"

    override fun evaluate(): Boolean = true // todo
}