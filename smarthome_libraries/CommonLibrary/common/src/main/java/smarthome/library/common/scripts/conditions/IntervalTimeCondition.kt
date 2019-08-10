package smarthome.library.common.scripts.conditions

open class IntervalTimeCondition : Condition() {
    val min = 5
    var interval = min

    override fun toString() = "At interval ${formatInterval()}"

    fun formatInterval(): String {
        val hours = interval / 60
        val minutes = interval % 60

        if (hours == 0) return "$minutes min"
        if (minutes == 0) return "$hours h"

        return "$hours h. $minutes min"
    }


    override fun evaluate(): Boolean = true // todo
}